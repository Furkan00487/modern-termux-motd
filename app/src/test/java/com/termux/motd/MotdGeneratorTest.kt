package com.termux.motd

import com.termux.motd.domain.BoxStyle
import com.termux.motd.domain.MotdConfig
import com.termux.motd.domain.MotdGenerator
import com.termux.motd.domain.MotdTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotdGeneratorTest {

    @Test
    fun testGenerateScriptContainsHeaderAndShebang() {
        val config = MotdConfig(title = "ÖZEL BAŞLIK", subtitle = "ALT BAŞLIK TESTİ")
        val script = MotdGenerator.generateScript(config)

        assertTrue(script.startsWith("#!/data/data/com.termux/files/usr/bin/bash"))
        assertTrue(script.contains("ÖZEL BAŞLIK"))
        assertTrue(script.contains("ALT BAŞLIK TESTİ"))
        assertTrue(script.contains("Modern Termux:MOTD"))
    }

    @Test
    fun testGenerateScriptThemeColors() {
        val cyanConfig = MotdConfig(theme = MotdTheme.NEON_CYAN)
        val cyanScript = MotdGenerator.generateScript(cyanConfig)
        assertTrue(cyanScript.contains("\\033[1;36m"))

        val matrixConfig = MotdConfig(theme = MotdTheme.RETRO_MATRIX)
        val matrixScript = MotdGenerator.generateScript(matrixConfig)
        assertTrue(matrixScript.contains("\\033[1;32m"))
    }

    @Test
    fun testGenerateScriptBoxStyles() {
        val doubleLine = MotdConfig(boxStyle = BoxStyle.DOUBLE_LINE)
        val doubleScript = MotdGenerator.generateScript(doubleLine)
        assertTrue(doubleScript.contains("╔"))
        assertTrue(doubleScript.contains("╗"))

        val rounded = MotdConfig(boxStyle = BoxStyle.ROUNDED)
        val roundedScript = MotdGenerator.generateScript(rounded)
        assertTrue(roundedScript.contains("╭"))
        assertTrue(roundedScript.contains("╮"))

        val minimal = MotdConfig(boxStyle = BoxStyle.MINIMAL)
        val minimalScript = MotdGenerator.generateScript(minimal)
        assertTrue(minimalScript.contains("┌"))
        assertTrue(minimalScript.contains("┐"))
    }

    @Test
    fun testGenerateScriptBatteryOnDemandBroadcast() {
        val withBattery = MotdConfig(showBattery = true)
        val scriptWith = MotdGenerator.generateScript(withBattery)
        assertTrue(scriptWith.contains("am broadcast -n com.termux.motd/.receiver.BatteryReceiver"))
        assertTrue(scriptWith.contains("motd_battery.txt"))
        assertTrue(scriptWith.contains("🔋 Pil:"))

        val withoutBattery = MotdConfig(showBattery = false)
        val scriptWithout = MotdGenerator.generateScript(withoutBattery)
        assertFalse(scriptWithout.contains("com.termux.motd/.receiver.BatteryReceiver"))
        assertFalse(scriptWithout.contains("🔋 Pil:"))
    }

    @Test
    fun testGenerateScriptCustomCommands() {
        val config = MotdConfig(customCommands = listOf("git status", "htop", "neofetch"))
        val script = MotdGenerator.generateScript(config)

        assertTrue(script.contains("git status"))
        assertTrue(script.contains("htop"))
        assertTrue(script.contains("neofetch"))
    }

    @Test
    fun testGenerateScriptRamAndStorageToggle() {
        val config = MotdConfig(showRam = true, showStorage = true, showUptime = true)
        val script = MotdGenerator.generateScript(config)

        assertTrue(script.contains("/proc/meminfo"))
        assertTrue(script.contains("MemAvailable"))
        assertTrue(script.contains("df -h /data"))
        assertTrue(script.contains("uptime -p"))
    }

    @Test
    fun testMotdConfigDynamicFieldsDefaults() {
        val config = MotdConfig()
        assertTrue(config.showSystemStatusHeader)
        org.junit.Assert.assertEquals("Sistem Durumu", config.systemStatusTitle)
        org.junit.Assert.assertEquals("🔋 Pil:", config.batteryLabel)
        org.junit.Assert.assertEquals("💾 Bellek (RAM):", config.ramLabel)
        org.junit.Assert.assertEquals("📁 Dahili Depolama:", config.storageLabel)
        org.junit.Assert.assertEquals("⏱️  Çalışma Süresi:", config.uptimeLabel)
        assertTrue(config.showCommandsHeader)
        org.junit.Assert.assertEquals("⚡ Hızlı Komutlar:", config.commandsTitle)
    }

    @Test
    fun testGenerateScriptCustomSectionTitlesAndLabels() {
        val customConfig = MotdConfig(
            systemStatusTitle = "DONANIM BİLGİSİ",
            batteryLabel = "BATARYA GÜCÜ:",
            showCommandsHeader = true,
            commandsTitle = "KISAYOLLAR:"
        )
        val script = MotdGenerator.generateScript(customConfig)
        assertTrue(script.contains("DONANIM BİLGİSİ"))
        assertTrue(script.contains("BATARYA GÜCÜ:"))
        assertTrue(script.contains("KISAYOLLAR:"))

        val hiddenConfig = MotdConfig(
            showSystemStatusHeader = false,
            showCommandsHeader = false
        )
        val hiddenScript = MotdGenerator.generateScript(hiddenConfig)
        assertFalse(hiddenScript.contains("[ Sistem Durumu ]"))
        assertFalse(hiddenScript.contains("⚡ Hızlı Komutlar:"))
    }
}
