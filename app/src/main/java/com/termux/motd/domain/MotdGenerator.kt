package com.termux.motd.domain

object MotdGenerator {

    fun generateScript(config: MotdConfig): String {
        val colorCode = config.theme.ansiColorCode
        val resetCode = "\\033[0m"
        val labelCode = "\\033[1;37m"
        val valCode = "\\033[0;32m"
        val cmdCode = "\\033[0;36m"

        val box = config.boxStyle
        val hLine = box.horizontal.repeat(54)

        val sb = StringBuilder()
        sb.append("#!/data/data/com.termux/files/usr/bin/bash\n")
        sb.append("# Modern Termux:MOTD - Otomatik Üretilen Dinamik Başlatıcı\n")
        sb.append("# Sıfır arka plan tüketimi: Yalnızca oturum açılışında anlık çalışır.\n\n")

        // Başlık Kutusu (Yalnızca başlık veya alt başlıktan biri doluysa çizilir)
        if (config.title.isNotBlank() || config.subtitle.isNotBlank()) {
            sb.append("echo -e \"${colorCode}${box.topLeft}${hLine}${box.topRight}${resetCode}\"\n")
            if (config.title.isNotBlank()) {
                sb.append("echo -e \"${colorCode}${box.vertical}${resetCode}  ${config.title.padEnd(50)}  ${colorCode}${box.vertical}${resetCode}\"\n")
            }
            if (config.subtitle.isNotBlank()) {
                sb.append("echo -e \"${colorCode}${box.vertical}${resetCode}  ${config.subtitle.padEnd(50)}  ${colorCode}${box.vertical}${resetCode}\"\n")
            }
            sb.append("echo -e \"${colorCode}${box.bottomLeft}${hLine}${box.bottomRight}${resetCode}\"\n\n")
        }

        // Dinamik Değişkenler
        if (config.showBattery) {
            sb.append("# Pil Bilgisi (On-Demand: Yalnızca açılışta veya refresh anında çalışır)\n")
            sb.append("BAT_INFO=\"\"\n")
            sb.append("# 1. Modern Termux:MOTD IPC köprüsü (0 izin, root gerekmez)\n")
            sb.append("am broadcast -n com.termux.motd/.receiver.BatteryReceiver >/dev/null 2>&1\n")
            sb.append("if [ -f \"/data/data/com.termux/files/usr/tmp/motd_battery.txt\" ]; then\n")
            sb.append("    BAT_INFO=\"$(cat /data/data/com.termux/files/usr/tmp/motd_battery.txt 2>/dev/null)\"\n")
            sb.append("fi\n")
            sb.append("# 2. Fallback: termux-battery-status veya sysfs\n")
            sb.append("if [ -z \"\$BAT_INFO\" ] || [ \"\$BAT_INFO\" = \"N/A\" ]; then\n")
            sb.append("    if [ -x \"/data/data/com.termux/files/usr/bin/termux-battery-status\" ]; then\n")
            sb.append("        BAT_INFO=\"\$(termux-battery-status 2>/dev/null | awk -F'[:,\"]+' '/percentage/ {p=\$2} /status/ {s=\$2} END {if(p) print p\"% (\" s \")\"}')\"\n")
            sb.append("    elif [ -r /sys/class/power_supply/battery/capacity ]; then\n")
            sb.append("        BAT_CAP=\"\$(cat /sys/class/power_supply/battery/capacity 2>/dev/null)\"\n")
            sb.append("        BAT_STAT=\"\"\n")
            sb.append("        [ -r /sys/class/power_supply/battery/status ] && BAT_STAT=\" (\$(cat /sys/class/power_supply/battery/status 2>/dev/null))\"\n")
            sb.append("        BAT_INFO=\"\${BAT_CAP}%\${BAT_STAT}\"\n")
            sb.append("    fi\n")
            sb.append("fi\n")
            sb.append("[ -z \"\$BAT_INFO\" ] && BAT_INFO=\"N/A\"\n\n")
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
        val hasAnyStat = config.showBattery || config.showRam || config.showStorage || config.showUptime
        if (hasAnyStat || config.showSystemStatusHeader) {
            if (config.showSystemStatusHeader) {
                if (config.systemStatusTitle.isNotBlank()) {
                    val titleLen = config.systemStatusTitle.length
                    val rightCount = maxOf(4, 52 - titleLen)
                    sb.append("echo -e \"${colorCode}${box.horizontal}${box.horizontal} [ ${config.systemStatusTitle} ] ${box.horizontal.repeat(rightCount)}${resetCode}\"\n")
                } else {
                    sb.append("echo -e \"${colorCode}${box.horizontal.repeat(57)}${resetCode}\"\n")
                }
            }
            if (config.showBattery) {
                sb.append("echo -e \" ${labelCode}${config.batteryLabel}${resetCode}               ${valCode}\$BAT_INFO${resetCode}\"\n")
            }
            if (config.showRam) {
                sb.append("echo -e \" ${labelCode}${config.ramLabel}${resetCode}      ${valCode}\$RAM_INFO${resetCode}\"\n")
            }
            if (config.showStorage) {
                sb.append("echo -e \" ${labelCode}${config.storageLabel}${resetCode}   ${valCode}\$DISK_INFO${resetCode}\"\n")
            }
            if (config.showUptime) {
                sb.append("echo -e \" ${labelCode}${config.uptimeLabel}${resetCode}   ${valCode}\$UPTIME_INFO${resetCode}\"\n")
            }
            val bottomDiv = box.horizontal.repeat(57)
            sb.append("echo -e \"${colorCode}${bottomDiv}${resetCode}\"\n\n")
        }

        // Hızlı Komutlar
        if (config.showCommands) {
            if (config.showCommandsHeader && config.commandsTitle.isNotBlank()) {
                sb.append("echo -e \"${labelCode}${config.commandsTitle}${resetCode}\"\n")
            }
            for (cmd in config.customCommands) {
                sb.append("echo -e \"   ${cmdCode}$cmd${resetCode}\"\n")
            }
            if (config.showCommandsHeader || config.customCommands.isNotEmpty()) {
                sb.append("echo \"\"\n")
            }
        }

        return sb.toString()
    }

    /**
     * Modern Termux terminal oturumu için seçilen temanın renginde dinamik prompt (PS1) betiği üretir.
     * Bash'in yerleşik '\$' kuralı sayesinde normal kullanıcıda '$', root olunduğunda otomatik '#' basar.
     */
    fun generatePromptScript(config: MotdConfig): String {
        return "#!/data/data/com.termux/files/usr/bin/bash\n" +
                "# Modern Termux:MOTD - Dinamik Tema Komut İstemi (Prompt)\n" +
                "export PS1='\\[\\e[0;32m\\]\\w\\[\\e[0m\\] \\[" + config.theme.ansiColorCode + "\\]\\$\\[\\e[0m\\] '\n"
    }
}
