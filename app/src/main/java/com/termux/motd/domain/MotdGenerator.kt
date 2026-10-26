package com.termux.motd.domain

object MotdGenerator {

    fun generateScript(config: MotdConfig): String {
        val colorCode = config.theme.ansiColorCode
        val resetCode = "\\033[0m"
        val labelCode = "\\033[1;37m"
        val valCode = "\\033[0;32m"
        val cmdCode = "\\033[0;36m"

        val sb = StringBuilder()
        sb.append("#!/data/data/com.termux/files/usr/bin/bash\n")
        sb.append("# Modern Termux:MOTD - Otomatik Üretilen Dinamik Başlatıcı\n")
        sb.append("# Sıfır arka plan tüketimi: Yalnızca oturum açılışında anlık çalışır.\n\n")

        // Başlık Kutusu
        sb.append("echo -e \"${colorCode}╔════════════════════════════════════════════════════════╗${resetCode}\"\n")
        sb.append("echo -e \"${colorCode}║${resetCode}  ${config.title.padEnd(52)}  ${colorCode}║${resetCode}\"\n")
        sb.append("echo -e \"${colorCode}╚════════════════════════════════════════════════════════╝${resetCode}\"\n\n")

        // Dinamik Değişkenler
        if (config.showBattery) {
            sb.append("# Pil Bilgisi\n")
            sb.append("BAT_INFO=\"N/A\"\n")
            sb.append("if [ -r /sys/class/power_supply/battery/capacity ]; then\n")
            sb.append("    BAT_CAP=\"$(cat /sys/class/power_supply/battery/capacity 2>/dev/null)\"\n")
            sb.append("    BAT_STAT=\"\"\n")
            sb.append("    [ -r /sys/class/power_supply/battery/status ] && BAT_STAT=\" ($(cat /sys/class/power_supply/battery/status 2>/dev/null))\"\n")
            sb.append("    BAT_INFO=\"\${BAT_CAP}%\${BAT_STAT}\"\n")
            sb.append("fi\n\n")
        }

        if (config.showRam) {
            sb.append("# RAM Bilgisi\n")
            sb.append("RAM_INFO=\"N/A\"\n")
            sb.append("if [ -r /proc/meminfo ]; then\n")
            sb.append("    RAM_AVAIL=\"$(awk '/MemAvailable/ {printf \"%.1f GB\", $2/1048576}' /proc/meminfo 2>/dev/null)\"\n")
            sb.append("    RAM_TOTAL=\"$(awk '/MemTotal/ {printf \"%.1f GB\", $2/1048576}' /proc/meminfo 2>/dev/null)\"\n")
            sb.append("    RAM_INFO=\"\${RAM_AVAIL} boş / \${RAM_TOTAL}\"\n")
            sb.append("fi\n\n")
        }

        if (config.showStorage) {
            sb.append("# Depolama Bilgisi\n")
            sb.append("DISK_INFO=\"$(df -h /data 2>/dev/null | awk 'NR==2 {print $4 \" boş / \" $2}')\"\n")
            sb.append("[ -z \"\$DISK_INFO\" ] && DISK_INFO=\"N/A\"\n\n")
        }

        if (config.showUptime) {
            sb.append("# Çalışma Süresi (Uptime)\n")
            sb.append("UPTIME_INFO=\"$(uptime -p 2>/dev/null | sed 's/^up //')\"\n")
            sb.append("[ -z \"\$UPTIME_INFO\" ] && UPTIME_INFO=\"N/A\"\n\n")
        }

        // Çıktı Tablosu
        sb.append("echo -e \"${colorCode}── [ Sistem Durumu ] ────────────────────────────────────${resetCode}\"\n")
        if (config.showBattery) {
            sb.append("echo -e \" ${labelCode}🔋 Pil:${resetCode}               ${valCode}\$BAT_INFO${resetCode}\"\n")
        }
        if (config.showRam) {
            sb.append("echo -e \" ${labelCode}💾 Bellek (RAM):${resetCode}      ${valCode}\$RAM_INFO${resetCode}\"\n")
        }
        if (config.showStorage) {
            sb.append("echo -e \" ${labelCode}📁 Dahili Depolama:${resetCode}   ${valCode}\$DISK_INFO${resetCode}\"\n")
        }
        if (config.showUptime) {
            sb.append("echo -e \" ${labelCode}⏱️  Çalışma Süresi:${resetCode}   ${valCode}\$UPTIME_INFO${resetCode}\"\n")
        }
        sb.append("echo -e \"${colorCode}─────────────────────────────────────────────────────────${resetCode}\"\n\n")

        // Hızlı Komutlar
        if (config.customCommands.isNotEmpty()) {
            sb.append("echo -e \"${labelCode}⚡ Hızlı Komutlar:${resetCode}\"\n")
            for (cmd in config.customCommands) {
                sb.append("echo -e \"   ${cmdCode}$cmd${resetCode}\"\n")
            }
            sb.append("echo \"\"\n")
        }

        return sb.toString()
    }
}
