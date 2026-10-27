package com.termux.motd.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.termux.motd.domain.MotdConfig
import com.termux.motd.domain.MotdTheme

@Composable
fun TerminalPreviewCard(
    config: MotdConfig,
    modifier: Modifier = Modifier
) {
    var isRootPrompt by remember { mutableStateOf(false) }

    val themeColor = when (config.theme) {
        MotdTheme.NEON_CYAN -> Color(0xFF00E5FF)
        MotdTheme.RETRO_MATRIX -> Color(0xFF00FF66)
        MotdTheme.CYBER_AMBER -> Color(0xFFFFB800)
        MotdTheme.MINIMAL_ICE -> Color(0xFF7AA2F7)
    }

    val box = config.boxStyle
    val titleLen = maxOf(config.title.length, config.subtitle.length)
    val boxWidth = maxOf(34, minOf(44, titleLen))
    val hLine = box.horizontal.repeat(boxWidth + 4)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF05070A))
            .border(1.dp, Color(0xFF1E2836), RoundedCornerShape(12.dp))
    ) {
        // Terminal Başlık Çubuğu
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0E131A))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFF5F56)))
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFFBD2E)))
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF27C93F)))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (config.isDefaultPreview) "termux ~ Orijinal Modern Termux" else "termux ~ ${config.theme.displayName}",
                    color = Color(0xFF8A99A8),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // İstem Değiştirici Buton ($ / #)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF151D28))
                    .clickable { isRootPrompt = !isRootPrompt }
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isRootPrompt) "Root (#)" else "User ($)",
                    color = themeColor,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Terminal İçeriği
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (config.isDefaultPreview) {
                // Orijinal Modern Termux Varsayılan MOTD Ekranı
                Text(
                    text = "╔════════════════════════════════════════════════════════╗",
                    color = themeColor,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
                Text(
                    text = "║              MODERN TERMUX'A HOŞ GELDİN!               ║",
                    color = themeColor,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
                Text(
                    text = "╚════════════════════════════════════════════════════════╝",
                    color = themeColor,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = " Paket aramak için:          pkg search <sorgu>",
                    color = Color(0xFFAAAAAA),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
                Text(
                    text = " Paket kurmak için:          pkg install <paket>",
                    color = Color(0xFFAAAAAA),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
                Text(
                    text = " Paketleri güncellemek için: pkg upgrade",
                    color = Color(0xFFAAAAAA),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
                Text(
                    text = " Sistem bilgisi:             termux-info",
                    color = Color(0xFFAAAAAA),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = " Topluluk & Destek: https://github.com/Furkan00487/modern-termux",
                    color = themeColor,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
            } else {
                // Dinamik Şablon Önizlemesi

                // 1. Başlık Kutusu (Yalnızca başlık veya alt başlık doluysa çizilir)
                if (config.title.isNotBlank() || config.subtitle.isNotBlank()) {
                    Text(
                        text = "${box.topLeft}${hLine}${box.topRight}",
                        color = themeColor,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                    if (config.title.isNotBlank()) {
                        Text(
                            text = "${box.vertical}  ${config.title.take(boxWidth).padEnd(boxWidth)}  ${box.vertical}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                    if (config.subtitle.isNotBlank()) {
                        Text(
                            text = "${box.vertical}  ${config.subtitle.take(boxWidth).padEnd(boxWidth)}  ${box.vertical}",
                            color = Color(0xFFAAAAAA),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                    }
                    Text(
                        text = "${box.bottomLeft}${hLine}${box.bottomRight}",
                        color = themeColor,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }

                // 2. Sistem Durumu Bölümü
                val hasAnyStat = config.showBattery || config.showRam || config.showStorage || config.showUptime
                if (hasAnyStat || config.showSystemStatusHeader) {
                    Spacer(modifier = Modifier.height(4.dp))
                    if (config.showSystemStatusHeader) {
                        val statusText = if (config.systemStatusTitle.isNotBlank()) " [ ${config.systemStatusTitle} ] " else " "
                        val remainingDashes = maxOf(2, 38 - statusText.length)
                        Text(
                            text = "${box.horizontal}${box.horizontal}$statusText${box.horizontal.repeat(remainingDashes)}",
                            color = themeColor,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }

                    if (config.showBattery) {
                        Text(
                            text = " ${config.batteryLabel.padEnd(16)} 88% (Şarj Oluyor ⚡)",
                            color = Color(0xFF00FF66),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                    if (config.showRam) {
                        Text(
                            text = " ${config.ramLabel.padEnd(16)} 4.2 GB boş / 8.0 GB",
                            color = Color(0xFF00FF66),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                    if (config.showStorage) {
                        Text(
                            text = " ${config.storageLabel.padEnd(16)} 45 GB boş / 128 GB",
                            color = Color(0xFF00FF66),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                    if (config.showUptime) {
                        Text(
                            text = " ${config.uptimeLabel.padEnd(16)} 3 days, 4 hours",
                            color = Color(0xFF00FF66),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }

                    if (!hasAnyStat) {
                        Text(
                            text = "   (Hiçbir modül seçilmedi)",
                            color = Color(0xFF667788),
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                    }

                    Text(
                        text = box.horizontal.repeat(38),
                        color = themeColor,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }

                // 3. Hızlı Komutlar Bölümü
                if (config.showCommands) {
                    Spacer(modifier = Modifier.height(2.dp))
                    if (config.showCommandsHeader && config.commandsTitle.isNotBlank()) {
                        Text(
                            text = config.commandsTitle,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                    if (config.customCommands.isNotEmpty()) {
                        config.customCommands.forEach { cmd ->
                            Text(
                                text = "   $cmd",
                                color = themeColor,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        }
                    } else {
                        Text(
                            text = "   (Henüz komut eklenmedi)",
                            color = Color(0xFF667788),
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // Komut İstemi ($ / #) - Tema Renginde ve Tıklanabilir!
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { isRootPrompt = !isRootPrompt }
            ) {
                Text(
                    text = if (isRootPrompt) "# " else "$ ",
                    color = themeColor,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
                Text(
                    text = "█",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
        }
    }
}
