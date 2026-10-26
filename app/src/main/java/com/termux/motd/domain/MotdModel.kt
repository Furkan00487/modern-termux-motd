package com.termux.motd.domain

enum class MotdTheme(
    val displayName: String,
    val description: String,
    val primaryColorHex: String,
    val accentColorHex: String,
    val ansiColorCode: String
) {
    NEON_CYAN(
        displayName = "Modern Neon Cyan",
        description = "Modern Termux ile tam uyumlu parlak camgöbeği",
        primaryColorHex = "#00E5FF",
        accentColorHex = "#FFFFFF",
        ansiColorCode = "\\033[1;36m"
    ),
    RETRO_MATRIX(
        displayName = "Retro Matrix",
        description = "Klasik fosforlu yeşil siber terminal teması",
        primaryColorHex = "#00FF66",
        accentColorHex = "#A3FFA3",
        ansiColorCode = "\\033[1;32m"
    ),
    CYBER_AMBER(
        displayName = "Cyber Amber",
        description = "Sıcak kehribar ve turuncu siberpunk tarzı",
        primaryColorHex = "#FFB800",
        accentColorHex = "#FFE599",
        ansiColorCode = "\\033[1;33m"
    ),
    MINIMAL_ICE(
        displayName = "Minimal Ice",
        description = "Sade, temiz ve ferah buz mavisi çizgiler",
        primaryColorHex = "#7AA2F7",
        accentColorHex = "#C0CAF5",
        ansiColorCode = "\\033[1;34m"
    )
}

data class MotdConfig(
    val title: String = "MODERN TERMUX'A HOŞ GELDİN!",
    val subtitle: String = "Sistem & Oturum Özeti",
    val theme: MotdTheme = MotdTheme.NEON_CYAN,
    val showBattery: Boolean = true,
    val showRam: Boolean = true,
    val showStorage: Boolean = true,
    val showUptime: Boolean = true,
    val customCommands: List<String> = listOf("pkg update", "termux-info", "termux-x11 :0 &")
)
