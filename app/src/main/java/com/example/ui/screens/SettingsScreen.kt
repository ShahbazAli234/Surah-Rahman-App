package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.islamic.IslamicContentData
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.GoldAccent
import com.example.ui.viewmodel.SurahRahmanViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: SurahRahmanViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val appTheme by viewModel.appTheme.collectAsState()
    val arabicFontSize by viewModel.arabicFontSize.collectAsState()
    val transFontSize by viewModel.transFontSize.collectAsState()
    val showEnglish by viewModel.showEnglish.collectAsState()
    val showUrdu by viewModel.showUrdu.collectAsState()
    val showSindhi by viewModel.showSindhi.collectAsState()
    val hapticEnabled by viewModel.hapticEnabled.collectAsState()
    val audioState by viewModel.audioState.collectAsState()

    var showResetDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Theme Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DarkMode, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("App Theme", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("SYSTEM" to "System", "DARK" to "Dark", "LIGHT" to "Light").forEach { (themeKey, label) ->
                            val selected = appTheme == themeKey
                            FilterChip(
                                selected = selected,
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.preferencesManager.setAppTheme(themeKey)
                                    }
                                },
                                label = { Text(label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GoldAccent.copy(alpha = 0.2f),
                                    selectedLabelColor = GoldAccent
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Typography & Font Sizes
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FormatSize, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Typography & Sizing", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Arabic Quran Font Size: ${arabicFontSize}sp", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = arabicFontSize.toFloat(),
                        onValueChange = { coroutineScope.launch { viewModel.preferencesManager.setArabicFontSize(it.toInt()) } },
                        valueRange = 20f..44f,
                        colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = GoldAccent)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Translation Font Size: ${transFontSize}sp", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = transFontSize.toFloat(),
                        onValueChange = { coroutineScope.launch { viewModel.preferencesManager.setTransFontSize(it.toInt()) } },
                        valueRange = 12f..24f,
                        colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = GoldAccent)
                    )
                }
            }
        }

        // Translation Preferences
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Translate, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Translation Languages", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    TranslationToggleRow(
                        title = "English (Sahih International)",
                        checked = showEnglish,
                        onCheckedChange = { coroutineScope.launch { viewModel.preferencesManager.setShowEnglish(it) } }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    TranslationToggleRow(
                        title = "Urdu (Fateh Muhammad Jalandhry)",
                        checked = showUrdu,
                        onCheckedChange = { coroutineScope.launch { viewModel.preferencesManager.setShowUrdu(it) } }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    TranslationToggleRow(
                        title = "Sindhi (Taj Mehmood Amroti)",
                        checked = showSindhi,
                        onCheckedChange = { coroutineScope.launch { viewModel.preferencesManager.setShowSindhi(it) } }
                    )
                }
            }
        }

        // Preferred Reciter
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Headphones, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Default Audio Reciter", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    IslamicContentData.reciters.forEach { reciter ->
                        val isSelected = audioState.reciter.id == reciter.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.switchReciter(reciter) }
                                .padding(vertical = 10.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(reciter.name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp)
                                Text(reciter.style, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // Offline Audio Storage & Cache Manager
        item {
            val isDownloaded = viewModel.audioManager.isSurahFullyDownloaded(audioState.reciter.id)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Audio Downloads & Cache", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Status: ${if (isDownloaded) "Surah Ar-Rahman (78 Ayahs) downloaded for offline listening" else "Streamed online / partial cache"}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (!isDownloaded) {
                            Button(
                                onClick = {
                                    viewModel.downloadSurahForReciter(audioState.reciter)
                                    Toast.makeText(context, "Downloading recitation...", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                            ) {
                                Text("Download 78 Ayahs", color = EmeraldDark, fontWeight = FontWeight.Bold)
                            }
                        }
                        OutlinedButton(
                            onClick = {
                                viewModel.clearDownloadedAudio(audioState.reciter.id)
                                Toast.makeText(context, "Audio cache cleared", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear Audio Cache")
                        }
                    }
                }
            }
        }

        // Haptic Feedback Setting
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Vibration, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Haptic Vibration", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Vibrate on Tasbih tap & milestones", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Switch(
                        checked = hapticEnabled,
                        onCheckedChange = { coroutineScope.launch { viewModel.preferencesManager.setHaptic(it) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = GoldAccent, checkedTrackColor = GoldAccent.copy(alpha = 0.5f))
                    )
                }
            }
        }

        // Authenticity & References Attribution Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Authoritative Religious Sources", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Arabic Text: King Fahd Complex for the Printing of the Holy Quran (Madinah Mushaf, Uthmani Script).\n" +
                               "• English Translation: Sahih International (Authoritative English interpretation).\n" +
                               "• Urdu Translation: Maulana Fateh Muhammad Jalandhry.\n" +
                               "• Sindhi Translation: Maulana Taj Mehmood Amroti.\n" +
                               "• Audio Recitations: Mishary Rashid Alafasy, Abdul Basit Abdul Samad, and Saad Al-Ghamdi via verified public high-quality educational sources.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Reset Settings Button
        item {
            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reset_settings_btn")
            ) {
                Icon(Icons.Default.RestartAlt, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Reset All Settings to Default", color = MaterialTheme.colorScheme.error)
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Settings?") },
            text = { Text("Are you sure you want to restore all font sizes, translations, and theme settings to default?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetAllSettings()
                        showResetDialog = false
                        Toast.makeText(context, "Settings reset to defaults", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun TranslationToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = GoldAccent)
        )
    }
}
