package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Verse
import com.example.ui.components.BismillahHeader
import com.example.ui.components.VerseActionBottomSheet
import com.example.ui.components.VerseCard
import com.example.ui.theme.GoldAccent
import com.example.ui.viewmodel.ScreenTab
import com.example.ui.viewmodel.SurahRahmanViewModel
import kotlinx.coroutines.launch

enum class VerseFilter {
    ALL, REFRAINS_ONLY, BOOKMARKS_ONLY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadScreen(
    viewModel: SurahRahmanViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val displayedVerses by viewModel.displayedVerses.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val audioState by viewModel.audioState.collectAsState()
    val selectedVerseForAction by viewModel.selectedVerseForAction.collectAsState()
    val isDistractionFree by viewModel.isDistractionFreeMode.collectAsState()

    val arabicFontSize by viewModel.arabicFontSize.collectAsState()
    val transFontSize by viewModel.transFontSize.collectAsState()
    val showEnglish by viewModel.showEnglish.collectAsState()
    val showUrdu by viewModel.showUrdu.collectAsState()
    val showSindhi by viewModel.showSindhi.collectAsState()
    val arabicOnly by viewModel.arabicOnly.collectAsState()
    val tajweedEnabled by viewModel.tajweedEnabled.collectAsState()
    val wordByWordEnabled by viewModel.wordByWordEnabled.collectAsState()

    var activeFilter by remember { mutableStateOf(VerseFilter.ALL) }
    var showDisplayControls by remember { mutableStateOf(false) }

    val bookmarkedVerseNums = remember(bookmarks) { bookmarks.map { it.verseNumber }.toSet() }
    val noteVerseNums = remember(notes) { notes.map { it.verseNumber }.toSet() }

    // Filter verses
    val filteredList = remember(displayedVerses, activeFilter, bookmarkedVerseNums) {
        when (activeFilter) {
            VerseFilter.ALL -> displayedVerses
            VerseFilter.REFRAINS_ONLY -> displayedVerses.filter { it.isRefrain }
            VerseFilter.BOOKMARKS_ONLY -> displayedVerses.filter { bookmarkedVerseNums.contains(it.verseNumber) }
        }
    }

    // Scroll to audio playing verse if needed
    LaunchedEffect(audioState.currentVerseNumber) {
        if (audioState.isPlaying && filteredList.any { it.verseNumber == audioState.currentVerseNumber }) {
            val targetIdx = filteredList.indexOfFirst { it.verseNumber == audioState.currentVerseNumber }
            if (targetIdx >= 0) {
                listState.animateScrollToItem((targetIdx + 1).coerceAtMost(filteredList.size))
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top Bar: Search, Display Controls Toggle, Distraction Free Toggle (Hidden in distraction-free mode)
            AnimatedVisibility(visible = !isDistractionFree) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("read_search_field"),
                        placeholder = { Text("Search Ayah # or words in AR, EN, UR, SD...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = GoldAccent)
                        },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear")
                                    }
                                }
                                IconButton(
                                    onClick = { showDisplayControls = !showDisplayControls },
                                    modifier = Modifier.testTag("tune_display_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = "Text Settings",
                                        tint = if (showDisplayControls) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Filter Chips Row (All 78, Refrains 31, Bookmarks)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = activeFilter == VerseFilter.ALL,
                            onClick = { activeFilter = VerseFilter.ALL },
                            label = { Text("All 78 Ayahs") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldAccent.copy(alpha = 0.2f),
                                selectedLabelColor = GoldAccent
                            )
                        )
                        FilterChip(
                            selected = activeFilter == VerseFilter.REFRAINS_ONLY,
                            onClick = { activeFilter = VerseFilter.REFRAINS_ONLY },
                            label = { Text("Refrains (31)") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldAccent.copy(alpha = 0.2f),
                                selectedLabelColor = GoldAccent
                            )
                        )
                        FilterChip(
                            selected = activeFilter == VerseFilter.BOOKMARKS_ONLY,
                            onClick = { activeFilter = VerseFilter.BOOKMARKS_ONLY },
                            label = { Text("Saved (${bookmarks.size})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldAccent.copy(alpha = 0.2f),
                                selectedLabelColor = GoldAccent
                            )
                        )
                    }

                    // Expandable Display Settings Panel (Font sizes, language toggles, Tajweed)
                    AnimatedVisibility(visible = showDisplayControls) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                // Arabic Font Size
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Arabic Font Size: ${arabicFontSize}sp", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Slider(
                                    value = arabicFontSize.toFloat(),
                                    onValueChange = { coroutineScope.launch { viewModel.preferencesManager.setArabicFontSize(it.toInt()) } },
                                    valueRange = 20f..44f,
                                    colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = GoldAccent)
                                )

                                // Translation Font Size
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Translation Size: ${transFontSize}sp", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Slider(
                                    value = transFontSize.toFloat(),
                                    onValueChange = { coroutineScope.launch { viewModel.preferencesManager.setTransFontSize(it.toInt()) } },
                                    valueRange = 12f..24f,
                                    colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = GoldAccent)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Language Toggles Row
                                Text("Visible Translations:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    FilterChip(
                                        selected = showEnglish && !arabicOnly,
                                        onClick = { coroutineScope.launch { viewModel.preferencesManager.setShowEnglish(!showEnglish) } },
                                        label = { Text("English") },
                                        enabled = !arabicOnly
                                    )
                                    FilterChip(
                                        selected = showUrdu && !arabicOnly,
                                        onClick = { coroutineScope.launch { viewModel.preferencesManager.setShowUrdu(!showUrdu) } },
                                        label = { Text("اردو") },
                                        enabled = !arabicOnly
                                    )
                                    FilterChip(
                                        selected = showSindhi && !arabicOnly,
                                        onClick = { coroutineScope.launch { viewModel.preferencesManager.setShowSindhi(!showSindhi) } },
                                        label = { Text("سنڌي") },
                                        enabled = !arabicOnly
                                    )
                                    FilterChip(
                                        selected = arabicOnly,
                                        onClick = { coroutineScope.launch { viewModel.preferencesManager.setArabicOnly(!arabicOnly) } },
                                        label = { Text("Arabic Only") }
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Vocabulary & Tajweed Toggles
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Word-by-word Breakdown", fontSize = 13.sp)
                                    Switch(
                                        checked = wordByWordEnabled,
                                        onCheckedChange = { coroutineScope.launch { viewModel.preferencesManager.setWordByWord(it) } },
                                        colors = SwitchDefaults.colors(checkedThumbColor = GoldAccent, checkedTrackColor = GoldAccent.copy(alpha = 0.5f))
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Distraction free banner notice
            if (isDistractionFree) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleDistractionFreeMode() },
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Distraction-Free Mode (Tap to exit)",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Icon(
                            imageVector = Icons.Default.FullscreenExit,
                            contentDescription = "Exit Distraction Free",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Verses List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("read_verses_list"),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    BismillahHeader(modifier = Modifier.padding(12.dp))
                }

                if (filteredList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No verses found matching '$searchQuery'",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredList, key = { it.verseNumber }) { verse ->
                        val isBookmarked = bookmarkedVerseNums.contains(verse.verseNumber)
                        val hasNote = noteVerseNums.contains(verse.verseNumber)
                        val isPlaying = audioState.isPlaying && audioState.currentVerseNumber == verse.verseNumber

                        VerseCard(
                            verse = verse,
                            arabicFontSize = arabicFontSize,
                            transFontSize = transFontSize,
                            showEnglish = showEnglish,
                            showUrdu = showUrdu,
                            showSindhi = showSindhi,
                            arabicOnly = arabicOnly,
                            tajweedEnabled = tajweedEnabled,
                            wordByWordEnabled = wordByWordEnabled,
                            isBookmarked = isBookmarked,
                            hasNote = hasNote,
                            isPlaying = isPlaying,
                            onPlayClick = {
                                viewModel.playVerseAudio(verse.verseNumber)
                            },
                            onBookmarkClick = {
                                viewModel.toggleBookmark(verse.verseNumber)
                            },
                            onActionClick = {
                                viewModel.selectVerseForAction(verse)
                            }
                        )
                    }
                }
            }
        }

        // Floating Action Buttons (Scroll to Top & Distraction-Free Toggle)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = { viewModel.toggleDistractionFreeMode() },
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = GoldAccent,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("distraction_free_fab")
            ) {
                Icon(
                    imageVector = if (isDistractionFree) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                    contentDescription = "Distraction-Free Reading Mode",
                    modifier = Modifier.size(20.dp)
                )
            }

            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                containerColor = GoldAccent,
                contentColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("scroll_top_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Verse Action Bottom Sheet
        selectedVerseForAction?.let { verse ->
            val isBookmarked = bookmarkedVerseNums.contains(verse.verseNumber)
            val currentNote = notes.find { it.verseNumber == verse.verseNumber }?.noteText ?: ""

            VerseActionBottomSheet(
                verse = verse,
                isBookmarked = isBookmarked,
                currentNote = currentNote,
                onDismiss = { viewModel.selectVerseForAction(null) },
                onPlay = {
                    viewModel.playVerseAudio(verse.verseNumber)
                },
                onToggleBookmark = {
                    viewModel.toggleBookmark(verse.verseNumber)
                },
                onSaveNote = { newNote ->
                    viewModel.saveVerseNote(verse.verseNumber, newNote)
                },
                onSetLastRead = {
                    viewModel.setLastReadVerse(verse.verseNumber)
                }
            )
        }
    }
}
