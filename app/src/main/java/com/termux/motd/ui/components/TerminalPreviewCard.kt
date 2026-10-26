package com.termux.motd.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
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
    val themeColor = when (config.theme) {
        MotdTheme.NEON_CYAN -> Color(0xFF00E5FF)
        MotdTheme.RETRO_MATRIX -> Color(0xFF00FF66)
        MotdTheme.CYBER_AMBER -> Color(0xFFFFB800)
        MotdTheme.MINIMAL_ICE -> Color(0xFF7AA2F7)
    }

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFF5F56)))
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFFBD2E)))
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF27C93F)))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "termux ~ ${config.theme.displayName}",
                color = Color(0xFF8A99A8),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Terminal İçeriği
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Başlık Kutusu
            Text(
                text = "╔══════════════════════════════════════╗",
                color = themeColor,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            )
            Text(
                text = "║  ${config.title.take(34).padEnd(34)}  ║",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            )
            Text(
                text = "╚══════════════════════════════════════╝",
                color = themeColor,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "── [ Sistem Durumu ] ──────────────────",
                color = themeColor,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            )

            if (config.showBattery) {
                Text(
                    text = " 🔋 Pil:          88% (Charging)",
                    color = Color(0xFF00FF66),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
            }
            if (config.showRam) {
                Text(
                    text = " 💾 RAM:          4.2 GB boş / 8.0 GB",
                    color = Color(0xFF00FF66),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
            }
            if (config.showStorage) {
                Text(
                    text = " 📁 Depolama:     45 GB boş / 128 GB",
                    color = Color(0xFF00FF66),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
            }
            if (config.showUptime) {
                Text(
                    text = " ⏱️  Çalışma:      3 days, 4 hours",
                    color = Color(0xFF00FF66),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
            }

            Text(
                text = "───────────────────────────────────────",
                color = themeColor,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            )

            if (config.customCommands.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "⚡ Hızlı Komutlar:",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
                config.customCommands.take(3).forEach { cmd ->
                    Text(
                        text = "   $cmd",
                        color = Color(0xFF00E5FF),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$ ",
                    color = Color(0xFF00E5FF),
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
