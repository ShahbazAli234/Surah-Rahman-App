package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Verse
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.GoldAccent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerseCard(
    verse: Verse,
    arabicFontSize: Int,
    transFontSize: Int,
    showEnglish: Boolean,
    showUrdu: Boolean,
    showSindhi: Boolean,
    arabicOnly: Boolean,
    tajweedEnabled: Boolean,
    wordByWordEnabled: Boolean,
    isBookmarked: Boolean,
    hasNote: Boolean,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showWordsExpanded by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            isPlaying -> GoldAccent
            verse.isRefrain -> GoldAccent.copy(alpha = 0.4f)
            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        },
        label = "border_color"
    )

    val cardBgColor by animateColorAsState(
        targetValue = when {
            isPlaying -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            verse.isRefrain -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            else -> MaterialTheme.colorScheme.surface
        },
        label = "card_bg"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isPlaying) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .combinedClickable(
                onClick = {},
                onLongClick = onActionClick
            )
            .testTag("verse_card_${verse.verseNumber}"),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPlaying) 6.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Verse Header: Ayah badge, refrain pill, action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AyahNumberBadge(
                        number = verse.verseNumber,
                        isRefrain = verse.isRefrain
                    )
                    if (verse.isRefrain) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GoldAccent.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "Refrain",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GoldAccent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    if (hasNote) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = "Has Note",
                            tint = GoldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onPlayClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("play_verse_${verse.verseNumber}")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Play Verse",
                            tint = if (isPlaying) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onBookmarkClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("bookmark_verse_${verse.verseNumber}")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onActionClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Verse Options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic Text with proper RTL layout
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    text = verse.arabicText,
                    fontFamily = AmiriFontFamily,
                    fontSize = arabicFontSize.sp,
                    lineHeight = (arabicFontSize * 1.7).sp,
                    fontWeight = FontWeight.Normal,
                    color = if (verse.isRefrain) GoldAccent else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Transliteration
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = verse.transliteration,
                fontSize = (transFontSize - 2).coerceAtLeast(11).sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                modifier = Modifier.fillMaxWidth()
            )

            // Translations (if not arabicOnly)
            if (!arabicOnly) {
                if (showEnglish) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "EN: ${verse.englishTranslation}",
                        fontSize = transFontSize.sp,
                        lineHeight = (transFontSize * 1.4).sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (showUrdu) {
                    Spacer(modifier = Modifier.height(6.dp))
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = "اردو: ${verse.urduTranslation}",
                            fontFamily = AmiriFontFamily,
                            fontSize = (transFontSize + 2).sp,
                            lineHeight = ((transFontSize + 2) * 1.5).sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (showSindhi) {
                    Spacer(modifier = Modifier.height(6.dp))
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = "سنڌي: ${verse.sindhiTranslation}",
                            fontFamily = AmiriFontFamily,
                            fontSize = (transFontSize + 2).sp,
                            lineHeight = ((transFontSize + 2) * 1.5).sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Word by Word Vocabulary Section (if available)
            if (wordByWordEnabled && verse.keyWords.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .combinedClickable { showWordsExpanded = !showWordsExpanded }
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = GoldAccent
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (showWordsExpanded) "Hide Vocabulary" else "View Word Meanings (${verse.keyWords.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = GoldAccent
                    )
                }

                AnimatedVisibility(
                    visible = showWordsExpanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        verse.keyWords.forEach { word ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${word.english} (${word.urdu})",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = word.arabic,
                                    fontFamily = AmiriFontFamily,
                                    fontSize = 16.sp,
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
