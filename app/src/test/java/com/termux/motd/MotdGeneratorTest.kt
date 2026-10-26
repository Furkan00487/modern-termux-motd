package com.termux.motd

import com.termux.motd.domain.MotdConfig
import com.termux.motd.domain.MotdGenerator
import com.termux.motd.domain.MotdTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotdGeneratorTest {

    @Test
    fun testGenerateScriptContainsHeaderAndShebang() {
        val config = MotdConfig(title = "ÖZEL BAŞLIK")
        val script = MotdGenerator.generateScript(config)

        assertTrue(script.startsWith("#!/data/data/com.termux/files/usr/bin/bash"))
        assertTrue(script.contains("ÖZEL BAŞLIK"))
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
    fun testGenerateScriptBatteryToggle() {
        val withBattery = MotdConfig(showBattery = true)
        val scriptWith = MotdGenerator.generateScript(withBattery)
        assertTrue(scriptWith.contains("/sys/class/power_supply/battery/capacity"))
        assertTrue(scriptWith.contains("🔋 Pil:"))

        val withoutBattery = MotdConfig(showBattery = false)
        val scriptWithout = MotdGenerator.generateScript(withoutBattery)
        assertFalse(scriptWithout.contains("/sys/class/power_supply/battery/capacity"))
        assertFalse(scriptWithout.contains("🔋 Pil:"))
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
}
