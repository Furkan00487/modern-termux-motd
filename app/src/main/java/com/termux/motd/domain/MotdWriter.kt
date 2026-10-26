package com.termux.motd.domain

import android.system.Os
import java.io.File

object MotdWriter {

    private const val TERMUX_PREFIX = "/data/data/com.termux/files/usr"
    private const val MOTD_DIR_PATH = "$TERMUX_PREFIX/etc/motd.d"
    private const val PROFILE_DIR_PATH = "$TERMUX_PREFIX/etc/profile.d"
    const val SCRIPT_NAME = "dyn-motd.sh"
    const val HOOK_NAME = "00-dyn-motd.sh"

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

            // 2. $PREFIX/etc/profile.d/00-dyn-motd.sh kancasını yaz (Terminal her açıldığında anlık çağırması için)
            val profileDir = File(PROFILE_DIR_PATH)
            if (!profileDir.exists()) {
                profileDir.mkdirs()
            }

            val hookFile = File(profileDir, HOOK_NAME)
            val hookContent = """
                #!/data/data/com.termux/files/usr/bin/bash
                # Modern Termux:MOTD - Oturum Açılış Kancası
                if [ -x "$MOTD_DIR_PATH/$SCRIPT_NAME" ]; then
                    "$MOTD_DIR_PATH/$SCRIPT_NAME"
                fi
            """.trimIndent()

            hookFile.writeText(hookContent, Charsets.UTF_8)

            try {
                Os.chmod(hookFile.absolutePath, 493) // 0755
            } catch (ignored: Exception) {
                hookFile.setExecutable(true, false)
            }

            Result.success(scriptFile.absolutePath)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isTermuxInstalled(): Boolean {
        return File(TERMUX_PREFIX).exists()
    }
}
