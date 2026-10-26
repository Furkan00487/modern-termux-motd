package com.termux.motd

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.termux.motd.domain.BoxStyle
import com.termux.motd.domain.MotdConfig
import com.termux.motd.domain.MotdTheme
import com.termux.motd.domain.MotdWriter
import com.termux.motd.ui.components.TerminalPreviewCard
import com.termux.motd.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ModernTermuxMotdTheme {
                MotdAppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotdAppScreen() {
    val context = LocalContext.current
    var config by remember { mutableStateOf(MotdConfig()) }
    var applyStatusMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(true) }
    var newCommandInput by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Modern Termux:MOTD",
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontSize = 18.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = NeonCyan
                ),
                windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Canlı Önizleme Başlığı
            Text(
                text = "CANLI TERMİNAL ÖNİZLEME",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )

            // Canlı Terminal Kartı
            TerminalPreviewCard(config = config)

            // Durum Bildirimi
            applyStatusMessage?.let { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSuccess) Color(0xFF0D2818) else Color(0xFF330C0C)
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(
                            if (isSuccess) GreenAccent else Color(0xFFFF5252)
                        )
                    )
                ) {
                    Text(
                        text = msg,
                        modifier = Modifier.padding(12.dp),
                        color = if (isSuccess) GreenAccent else Color(0xFFFF5252),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Metin Özelleştirmeleri
            Text(
                text = "METİN VE BAŞLIKLAR",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )

            OutlinedTextField(
                value = config.title,
                onValueChange = { config = config.copy(title = it) },
                label = { Text("Karşılama Başlığı", color = TextMuted) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = Color(0xFF1E2836),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            OutlinedTextField(
                value = config.subtitle,
                onValueChange = { config = config.copy(subtitle = it) },
                label = { Text("Alt Başlık & Özel Not (Örn: Slogan, Sunucu)", color = TextMuted) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = Color(0xFF1E2836),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            // Çerçeve & Kutu Stili
            Text(
                text = "ÇERÇEVE & KUTU STİLİ",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BoxStyle.values().forEach { style ->
                    val isSelected = config.boxStyle == style
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { config = config.copy(boxStyle = style) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF131D28) else DarkSurface
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(
                                if (isSelected) NeonCyan else Color(0xFF1C2530)
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${style.topLeft}${style.horizontal}${style.topRight}",
                                color = if (isSelected) NeonCyan else Color.White,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = style.displayName,
                                color = if (isSelected) NeonCyan else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Tema Seçimi
            Text(
                text = "RENK & TEMA ŞABLONU",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MotdTheme.values().forEach { theme ->
                    val isSelected = config.theme == theme
                    val themeColor = Color(android.graphics.Color.parseColor(theme.primaryColorHex))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF131D28) else DarkSurface)
                            .border(
                                1.dp,
                                if (isSelected) NeonCyan else Color(0xFF1C2530),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { config = config.copy(theme = theme) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(themeColor)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = theme.displayName,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = theme.description,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Seçili",
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Dinamik Sistem Modülleri
            Text(
                text = "DİNAMİK SİSTEM MODÜLLERİ (ON-DEMAND: %0 PİL YÜKÜ)",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1C2530))
                )
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ModuleToggleRow(
                        title = "🔋 Pil Durumu (Şarj & Yüzde)",
                        checked = config.showBattery,
                        onCheckedChange = { config = config.copy(showBattery = it) }
                    )
                    HorizontalDivider(color = Color(0xFF1C2530), thickness = 0.5.dp)
                    ModuleToggleRow(
                        title = "💾 Bellek (Kullanılabilir / Toplam RAM)",
                        checked = config.showRam,
                        onCheckedChange = { config = config.copy(showRam = it) }
                    )
                    HorizontalDivider(color = Color(0xFF1C2530), thickness = 0.5.dp)
                    ModuleToggleRow(
                        title = "📁 Depolama (Dahili Disk Boş Alanı)",
                        checked = config.showStorage,
                        onCheckedChange = { config = config.copy(showStorage = it) }
                    )
                    HorizontalDivider(color = Color(0xFF1C2530), thickness = 0.5.dp)
                    ModuleToggleRow(
                        title = "⏱️ Çalışma Süresi (Uptime)",
                        checked = config.showUptime,
                        onCheckedChange = { config = config.copy(showUptime = it) }
                    )
                }
            }

            // Hızlı Komutlar Yöneticisi
            Text(
                text = "HIZLI KOMUTLAR YÖNETİCİSİ",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1C2530))
                )
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (config.customCommands.isEmpty()) {
                        Text(
                            text = "Henüz hızlı komut eklenmedi.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    } else {
                        config.customCommands.forEachIndexed { index, cmd ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF131D28))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = cmd,
                                    color = NeonCyan,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp
                                )
                                IconButton(
                                    onClick = {
                                        val updated = config.customCommands.toMutableList()
                                        updated.removeAt(index)
                                        config = config.copy(customCommands = updated)
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Sil",
                                        tint = Color(0xFFFF5252),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Yeni Komut Ekleme Satırı
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newCommandInput,
                            onValueChange = { newCommandInput = it },
                            placeholder = { Text("Yeni komut (örn: git status)", color = TextMuted, fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = Color(0xFF1E2836),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        IconButton(
                            onClick = {
                                val trimmed = newCommandInput.trim()
                                if (trimmed.isNotBlank()) {
                                    config = config.copy(customCommands = config.customCommands + trimmed)
                                    newCommandInput = ""
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeonCyan)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Ekle",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }

            // Uygula Butonu
            Button(
                onClick = {
                    val result = MotdWriter.applyMotd(config)
                    if (result.isSuccess) {
                        isSuccess = true
                        applyStatusMessage = "✅ Başarılı! MOTD şablonu Modern Termux'a kaydedildi.\nTerminalde anında görmek için 'termux-motd-refresh' çalıştırabilir veya yeni sekme açabilirsiniz."
                        Toast.makeText(context, "Modern Termux'a uygulandı!", Toast.LENGTH_SHORT).show()
                    } else {
                        isSuccess = false
                        val error = result.exceptionOrNull()?.message ?: "Bilinmeyen hata"
                        applyStatusMessage = "❌ Hata oluştu: $error\nModern Termux'un kurulu ve aynı sharedUserId ile imzalı olduğundan emin olun."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonCyan,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Modern Termux'a Uygula",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            // Varsayılan MOTD'a Sıfırla Butonu
            OutlinedButton(
                onClick = {
                    val result = MotdWriter.resetToDefault()
                    if (result.isSuccess) {
                        config = MotdConfig()
                        isSuccess = true
                        applyStatusMessage = "🔄 Modern Termux varsayılan MOTD'una başarıyla sıfırlandı!\nTerminali açtığınızda orijinal karşılama ekranı görünecektir."
                        Toast.makeText(context, "Varsayılan MOTD'a sıfırlandı!", Toast.LENGTH_SHORT).show()
                    } else {
                        isSuccess = false
                        val error = result.exceptionOrNull()?.message ?: "Bilinmeyen hata"
                        applyStatusMessage = "❌ Sıfırlama hatası: $error"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TextMuted
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E2836)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = TextMuted
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Varsayılan MOTD'a Sıfırla",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ModuleToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = NeonCyan,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = DarkBackground
            )
        )
    }
}
