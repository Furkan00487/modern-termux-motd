package com.termux.motd.domain

import android.system.Os
import java.io.File

object MotdWriter {

    private const val TERMUX_HOME = "/data/data/com.termux/files/home"
    private const val TERMUX_PREFIX = "/data/data/com.termux/files/usr"
    private const val MOTD_DIR_PATH = "$TERMUX_PREFIX/etc/motd.d"
    private const val PROFILE_DIR_PATH = "$TERMUX_PREFIX/etc/profile.d"
    private const val TERMUX_CONFIG_DIR = "$TERMUX_HOME/.termux"
    private const val STATIC_MOTD_PATH = "$TERMUX_PREFIX/etc/motd"
    const val SCRIPT_NAME = "dyn-motd.sh"
    const val HOOK_NAME = "00-dyn-motd.sh"
    const val USER_MOTD_NAME = "motd.sh"

    // Modern Termux kaynak kodundaki (TermuxInstaller.java) orijinal varsayılan MOTD içeriği
    const val DEFAULT_MODERN_TERMUX_MOTD =
        "\u001B[1;34m╔════════════════════════════════════════════════════════╗\n" +
        "║              MODERN TERMUX'A HOŞ GELDİN!               ║\n" +
        "╚════════════════════════════════════════════════════════╝\u001B[0m\n\n" +
        " Paket aramak için:          \u001B[1;32mpkg search <sorgu>\u001B[0m\n" +
        " Paket kurmak için:          \u001B[1;32mpkg install <paket>\u001B[0m\n" +
        " Paketleri güncellemek için: \u001B[1;32mpkg upgrade\u001B[0m\n" +
        " Sistem bilgisi:             \u001B[1;32mtermux-info\u001B[0m\n\n" +
        " Topluluk & Destek: https://github.com/Furkan00487/modern-termux\n\n"

    fun applyMotd(config: MotdConfig): Result<String> {
        return try {
            val scriptContent = MotdGenerator.generateScript(config)

            // 1. $PREFIX/etc/motd.d/dyn-motd.sh dosyasını yaz
            val motdDir = File(MOTD_DIR_PATH)
            if (!motdDir.exists()) {
                motdDir.mkdirs()
            }

            val scriptFile = File(motdDir, SCRIPT_NAME)
            scriptFile.writeText(scriptContent, Charsets.UTF_8)

            // Dosyaya çalıştırma yetkisi ver (chmod 755 / rwxr-xr-x)
            try {
                Os.chmod(scriptFile.absolutePath, 493) // 0755
            } catch (ignored: Exception) {
                scriptFile.setExecutable(true, false)
            }

            // 2. ~/.termux/motd.sh kancasını yaz
            // Termux'un yerleşik login scripti ~/.termux/motd.sh olduğunda onu çalıştırır
            // ve 'elif [ -f .../etc/motd ]' yapısı sayesinde eski statik MOTD'u otomatik olarak bastırır (gizler)!
            val userTermuxDir = File(TERMUX_CONFIG_DIR)
            if (!userTermuxDir.exists()) {
                userTermuxDir.mkdirs()
            }

            val userMotdFile = File(userTermuxDir, USER_MOTD_NAME)
            val userMotdContent = """
                #!/data/data/com.termux/files/usr/bin/bash
                if [ -x "$MOTD_DIR_PATH/$SCRIPT_NAME" ]; then
                    "$MOTD_DIR_PATH/$SCRIPT_NAME"
                fi
            """.trimIndent()

            userMotdFile.writeText(userMotdContent, Charsets.UTF_8)
            try {
                Os.chmod(userMotdFile.absolutePath, 493) // 0755
            } catch (_: Exception) {
                userMotdFile.setExecutable(true, false)
            }

            // Çift çalışma ve çakışmayı önlemek için eski profile.d kancasını temizle
            try {
                File(PROFILE_DIR_PATH, HOOK_NAME).delete()
            } catch (_: Exception) {
            }

            Result.success(scriptFile.absolutePath)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun resetToDefault(): Result<Unit> {
        return try {
            // 1. Dinamik MOTD kancalarını ve scriptlerini temizle
            try {
                File(TERMUX_CONFIG_DIR, USER_MOTD_NAME).delete()
                File(MOTD_DIR_PATH, SCRIPT_NAME).delete()
                File(PROFILE_DIR_PATH, HOOK_NAME).delete()
                File(TERMUX_HOME, ".hushlogin").delete()
            } catch (_: Exception) {
            }

            // 2. Orijinal Modern Termux varsayılan MOTD dosyasını geri yükle
            val staticMotdFile = File(STATIC_MOTD_PATH)
            staticMotdFile.parentFile?.mkdirs()
            staticMotdFile.writeText(DEFAULT_MODERN_TERMUX_MOTD, Charsets.UTF_8)
            try {
                Os.chmod(staticMotdFile.absolutePath, 420) // 0644
            } catch (_: Exception) {
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isTermuxInstalled(): Boolean {
        return File(TERMUX_PREFIX).exists()
    }
}
