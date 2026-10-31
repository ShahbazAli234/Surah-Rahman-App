package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Verse
import com.example.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerseActionBottomSheet(
    verse: Verse,
    isBookmarked: Boolean,
    currentNote: String,
    onDismiss: () -> Unit,
    onPlay: () -> Unit,
    onToggleBookmark: () -> Unit,
    onSaveNote: (String) -> Unit,
    onSetLastRead: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isEditingNote by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf(currentNote) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AyahNumberBadge(number = verse.verseNumber, isRefrain = verse.isRefrain)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Surah Ar-Rahman — Ayah ${verse.verseNumber}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = verse.transliteration,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(8.dp))

            if (isEditingNote) {
                // Note editing mode
                Text(
                    text = "Personal Reflection / Note",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = GoldAccent
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Write your thoughts or reflections for Ayah ${verse.verseNumber}...") },
                    minLines = 3,
                    maxLines = 6
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentNote.isNotBlank()) {
                        IconButton(
                            onClick = {
                                onSaveNote("")
                                isEditingNote = false
                                Toast.makeText(context, "Note removed", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Note", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(onClick = { isEditingNote = false }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSaveNote(noteText)
                            isEditingNote = false
                            Toast.makeText(context, "Note saved", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save")
                    }
                }
            } else {
                // Actions List
                ActionItem(
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    title = "Play Recitation for this Ayah",
                    onClick = {
                        onPlay()
                        onDismiss()
                    }
                )

                ActionItem(
                    icon = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    title = if (isBookmarked) "Remove from Bookmarks" else "Bookmark this Ayah",
                    tint = if (isBookmarked) GoldAccent else MaterialTheme.colorScheme.onSurface,
                    onClick = {
                        onToggleBookmark()
                        Toast.makeText(context, if (isBookmarked) "Bookmark removed" else "Ayah bookmarked", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                )

                ActionItem(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    title = "Set as Last Read Position",
                    onClick = {
                        onSetLastRead()
                        Toast.makeText(context, "Marked Ayah ${verse.verseNumber} as last read", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                )

                ActionItem(
                    icon = Icons.AutoMirrored.Filled.Note,
                    title = if (currentNote.isNotBlank()) "Edit Note (${currentNote.take(20)}...)" else "Add Reflection / Note",
                    onClick = {
                        isEditingNote = true
                    }
                )

                ActionItem(
                    icon = Icons.Default.ContentCopy,
                    title = "Copy Arabic Text",
                    onClick = {
                        copyToClipboard(context, "Ayah ${verse.verseNumber} Arabic", verse.arabicText)
                        Toast.makeText(context, "Arabic text copied", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                )

                ActionItem(
                    icon = Icons.Default.ContentCopy,
                    title = "Copy All Translations (EN, UR, SD)",
                    onClick = {
                        val fullText = """
                            Surah Ar-Rahman (55:${verse.verseNumber})
                            ${verse.arabicText}

                            [English]
                            ${verse.englishTranslation}

                            [Urdu]
                            ${verse.urduTranslation}

                            [Sindhi]
                            ${verse.sindhiTranslation}
                        """.trimIndent()
                        copyToClipboard(context, "Ayah ${verse.verseNumber}", fullText)
                        Toast.makeText(context, "Translations copied to clipboard", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                )

                ActionItem(
                    icon = Icons.Default.Share,
                    title = "Share Ayah",
                    onClick = {
                        shareAyah(context, verse)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun ActionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                color = tint,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

private fun shareAyah(context: Context, verse: Verse) {
    val shareBody = """
        بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
        Surah Ar-Rahman [55:${verse.verseNumber}]

        ${verse.arabicText}

        English: ${verse.englishTranslation}
        اردو: ${verse.urduTranslation}
        سنڌي: ${verse.sindhiTranslation}

        — Shared from Surah Ar-Rahman App
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Surah Ar-Rahman [55:${verse.verseNumber}]")
        putExtra(Intent.EXTRA_TEXT, shareBody)
    }
    context.startActivity(Intent.createChooser(intent, "Share Ayah via"))
}
