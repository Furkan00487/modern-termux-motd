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

enum class BoxStyle(
    val displayName: String,
    val topLeft: String,
    val topRight: String,
    val bottomLeft: String,
    val bottomRight: String,
    val horizontal: String,
    val vertical: String
) {
    DOUBLE_LINE(
        displayName = "Çift Çizgi",
        topLeft = "╔",
        topRight = "╗",
        bottomLeft = "╚",
        bottomRight = "╝",
        horizontal = "═",
        vertical = "║"
    ),
    ROUNDED(
        displayName = "Yuvarlak Köşe",
        topLeft = "╭",
        topRight = "╮",
        bottomLeft = "╰",
        bottomRight = "╯",
        horizontal = "─",
        vertical = "│"
    ),
    MINIMAL(
        displayName = "Minimal İnce",
        topLeft = "┌",
        topRight = "┐",
        bottomLeft = "└",
        bottomRight = "┘",
        horizontal = "─",
        vertical = "│"
    ),
    CYBER_BLOCK(
        displayName = "Siber Blok",
        topLeft = "▛",
        topRight = "▜",
        bottomLeft = "▙",
        bottomRight = "▟",
        horizontal = "▀",
        vertical = "▌"
    )
}

data class MotdConfig(
    val title: String = "MODERN TERMUX'A HOŞ GELDİN!",
    val subtitle: String = "Sistem & Oturum Özeti",
    val theme: MotdTheme = MotdTheme.NEON_CYAN,
    val boxStyle: BoxStyle = BoxStyle.DOUBLE_LINE,

    // Sistem Durumu Başlığı & Modül Etiketleri
    val showSystemStatusHeader: Boolean = true,
    val systemStatusTitle: String = "Sistem Durumu",
    val showBattery: Boolean = true,
    val batteryLabel: String = "🔋 Pil:",
    val showRam: Boolean = true,
    val ramLabel: String = "💾 Bellek (RAM):",
    val showStorage: Boolean = true,
    val storageLabel: String = "📁 Dahili Depolama:",
    val showUptime: Boolean = true,
    val uptimeLabel: String = "⏱️  Çalışma Süresi:",

    // Hızlı Komutlar Başlığı
    val showCommandsHeader: Boolean = true,
    val commandsTitle: String = "⚡ Hızlı Komutlar:",
    val customCommands: List<String> = listOf("pkg update", "termux-info", "termux-x11 :0 &")
)
