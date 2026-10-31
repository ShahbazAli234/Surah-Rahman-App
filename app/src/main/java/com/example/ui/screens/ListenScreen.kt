package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.islamic.IslamicContentData
import com.example.data.quran.SurahRahmanProvider
import com.example.ui.components.AyahNumberBadge
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.GoldAccent
import com.example.ui.viewmodel.SurahRahmanViewModel
import java.util.Locale

@Composable
fun ListenScreen(
    viewModel: SurahRahmanViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val audioState by viewModel.audioState.collectAsState()
    val allVerses = viewModel.quranRepository.allVerses
    val currentVerse = SurahRahmanProvider.getVerse(audioState.currentVerseNumber) ?: allVerses.first()
    val reciters = IslamicContentData.reciters

    var showPlaylistDialog by remember { mutableStateOf(false) }

    // Rotating animation when audio is playing
    val infiniteTransition = rememberInfiniteTransition(label = "disc_rotate")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Reciter Selection Chips
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select Reciter (القراء):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(reciters) { reciter ->
                        val selected = audioState.reciter.id == reciter.id
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.switchReciter(reciter) },
                            label = {
                                Column(modifier = Modifier.padding(vertical = 2.dp)) {
                                    Text(reciter.name, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                                    Text(reciter.arabicName, fontFamily = AmiriFontFamily, fontSize = 11.sp, color = GoldAccent)
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldAccent.copy(alpha = 0.2f),
                                selectedLabelColor = GoldAccent
                            ),
                            modifier = Modifier.testTag("reciter_${reciter.id}")
                        )
                    }
                }
            }
        }

        // Animated Rotating Artwork / Medallion
        item {
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(2.dp, GoldAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_surah_icon),
                    contentDescription = "Rotating Quran Recitation Medallion",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .clip(CircleShape)
                        .rotate(if (audioState.isPlaying) rotationAngle else 0f)
                )

                if (audioState.isBuffering) {
                    CircularProgressIndicator(
                        color = GoldAccent,
                        modifier = Modifier.size(54.dp)
                    )
                }
            }
        }

        // Synced Verse Display Card (Active Ayah)
        item {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AyahNumberBadge(
                            number = currentVerse.verseNumber,
                            isRefrain = currentVerse.isRefrain
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Ayah ${currentVerse.verseNumber} of 78",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentVerse.arabicText,
                        fontFamily = AmiriFontFamily,
                        fontSize = 26.sp,
                        lineHeight = 42.sp,
                        color = if (currentVerse.isRefrain) GoldAccent else MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentVerse.transliteration,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentVerse.englishTranslation,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = currentVerse.urduTranslation,
                        fontFamily = AmiriFontFamily,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Progress Slider & Timestamps
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                val currentPos = audioState.currentPositionMs.toFloat()
                val totalDur = audioState.durationMs.toFloat().coerceAtLeast(1f)
                val sliderValue = (currentPos / totalDur).coerceIn(0f, 1f)

                Slider(
                    value = sliderValue,
                    onValueChange = { fraction ->
                        val targetMs = (fraction * totalDur).toLong()
                        viewModel.audioManager.seekToPosition(targetMs)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = GoldAccent,
                        activeTrackColor = GoldAccent,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("audio_slider")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatMs(audioState.currentPositionMs),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (audioState.durationMs > 0) formatMs(audioState.durationMs) else "--:--",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Primary Playback Controls Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Previous Ayah
                IconButton(
                    onClick = { viewModel.audioManager.skipToPrevious() },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("btn_skip_prev")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous Ayah",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Rewind 10s
                IconButton(
                    onClick = {
                        val newPos = (audioState.currentPositionMs - 10000L).coerceAtLeast(0L)
                        viewModel.audioManager.seekToPosition(newPos)
                    },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FastRewind,
                        contentDescription = "Rewind 10 seconds",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Animated Play / Pause FAB
                FloatingActionButton(
                    onClick = { viewModel.audioManager.togglePlayPause() },
                    containerColor = GoldAccent,
                    contentColor = EmeraldDark,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(68.dp)
                        .border(2.dp, GoldAccent.copy(alpha = 0.5f), CircleShape)
                        .testTag("btn_play_pause")
                ) {
                    Icon(
                        imageVector = if (audioState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (audioState.isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Fast Forward 10s
                IconButton(
                    onClick = {
                        val newPos = audioState.currentPositionMs + 10000L
                        viewModel.audioManager.seekToPosition(newPos)
                    },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = "Forward 10 seconds",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Next Ayah
                IconButton(
                    onClick = { viewModel.audioManager.skipToNext() },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("btn_skip_next")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Ayah",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // Secondary Controls (Speed, Repeat Mode, Sleep Timer, Playlist)
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
                        .padding(14.dp)
                ) {
                    // Speed Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Playback Speed:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf(0.75f, 1.0f, 1.25f, 1.5f).forEach { spd ->
                                val selected = audioState.playbackSpeed == spd
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (selected) GoldAccent else MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.clickable { viewModel.setPlaybackSpeed(spd) }
                                ) {
                                    Text(
                                        text = "${spd}x",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) EmeraldDark else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Repeat Mode & Sleep Timer Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Repeat mode toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val nextMode = when (audioState.repeatMode) {
                                    "SURAH" -> "VERSE"
                                    "VERSE" -> "OFF"
                                    else -> "SURAH"
                                }
                                viewModel.setRepeatMode(nextMode)
                            }
                        ) {
                            Icon(
                                imageVector = if (audioState.repeatMode == "VERSE") Icons.Default.RepeatOne else Icons.Default.Repeat,
                                contentDescription = null,
                                tint = if (audioState.repeatMode != "OFF") GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Repeat: ${audioState.repeatMode}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (audioState.repeatMode != "OFF") GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Sleep Timer Toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val nextTimer = when (audioState.sleepTimerRemainingMinutes) {
                                    null -> 15
                                    15 -> 30
                                    30 -> 60
                                    else -> null
                                }
                                viewModel.audioManager.setSleepTimer(nextTimer)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = if (audioState.sleepTimerRemainingMinutes != null) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (audioState.sleepTimerRemainingMinutes != null) "Timer: ${audioState.sleepTimerRemainingMinutes}m" else "Timer: Off",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (audioState.sleepTimerRemainingMinutes != null) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Offline Download Section
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
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                contentDescription = null,
                                tint = if (isDownloaded) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isDownloaded) "Downloaded for Offline Use" else "Download Surah Offline",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Reciter: ${audioState.reciter.name}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (audioState.isDownloading) {
                            CircularProgressIndicator(
                                progress = { audioState.downloadProgressPercent / 100f },
                                modifier = Modifier.size(28.dp),
                                color = GoldAccent
                            )
                        } else if (!isDownloaded) {
                            TextButton(
                                onClick = { viewModel.downloadSurahForReciter(audioState.reciter) },
                                modifier = Modifier.testTag("download_surah_btn")
                            ) {
                                Text("Download", color = GoldAccent, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            TextButton(
                                onClick = { viewModel.clearDownloadedAudio(audioState.reciter.id) }
                            ) {
                                Text("Clear", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }
                        }
                    }

                    if (audioState.isDownloading) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { audioState.downloadProgressPercent / 100f },
                            modifier = Modifier.fillMaxWidth(),
                            color = GoldAccent
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Downloading audio: ${audioState.downloadProgressPercent}%",
                            fontSize = 11.sp,
                            color = GoldAccent
                        )
                    }
                }
            }
        }
    }
}

private fun formatMs(ms: Long): String {
    val totalSeconds = (ms / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.US, "%02d:%02d", minutes, seconds)
}
