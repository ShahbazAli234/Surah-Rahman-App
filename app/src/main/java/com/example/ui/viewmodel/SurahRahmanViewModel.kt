package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.islamic.IslamicContentData
import com.example.data.islamic.allahNamesList
import com.example.data.local.BookmarkEntity
import com.example.data.local.PreferencesManager
import com.example.data.local.VerseNoteEntity
import com.example.data.model.AllahName
import com.example.data.model.DuaItem
import com.example.data.model.PredefinedDhikr
import com.example.data.model.Reciter
import com.example.data.model.Verse
import com.example.data.repository.CityLocation
import com.example.data.repository.PrayerRepository
import com.example.data.repository.QuranRepository
import com.example.service.AudioPlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME, READ, LISTEN, TOOLS, SETTINGS
}

enum class ToolsSubTab {
    TASBIH, PRAYERS, QIBLA, NAMES_OF_ALLAH, DUAS, EVENTS
}

class SurahRahmanViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context get() = getApplication<Application>().applicationContext
    val quranRepository = QuranRepository(context)
    val preferencesManager = PreferencesManager(context)
    val prayerRepository = PrayerRepository()
    val audioManager = AudioPlaybackManager(context, quranRepository)

    // Navigation State
    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _toolsSubTab = MutableStateFlow(ToolsSubTab.TASBIH)
    val toolsSubTab: StateFlow<ToolsSubTab> = _toolsSubTab.asStateFlow()

    // Full screen distraction-free reading mode
    private val _isDistractionFreeMode = MutableStateFlow(false)
    val isDistractionFreeMode: StateFlow<Boolean> = _isDistractionFreeMode.asStateFlow()

    // Search query in Read Screen
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Active city for prayer times
    private val _selectedCity = MutableStateFlow(prayerRepository.popularCities.first())
    val selectedCity: StateFlow<CityLocation> = _selectedCity.asStateFlow()

    // Qibla Compass azimuth
    private val _compassAzimuth = MutableStateFlow(0f)
    val compassAzimuth: StateFlow<Float> = _compassAzimuth.asStateFlow()

    // Selected Dhikr in Tasbih
    private val _selectedDhikr = MutableStateFlow(IslamicContentData.predefinedDhikrList.first())
    val selectedDhikr: StateFlow<PredefinedDhikr> = _selectedDhikr.asStateFlow()

    private val _customDhikrTarget = MutableStateFlow(33)
    val customDhikrTarget: StateFlow<Int> = _customDhikrTarget.asStateFlow()

    // Selected verse for action dialog / note sheet
    private val _selectedVerseForAction = MutableStateFlow<Verse?>(null)
    val selectedVerseForAction: StateFlow<Verse?> = _selectedVerseForAction.asStateFlow()

    // 99 Names search
    private val _namesSearchQuery = MutableStateFlow("")
    val namesSearchQuery: StateFlow<String> = _namesSearchQuery.asStateFlow()

    // Duas category filter
    private val _selectedDuaCategory = MutableStateFlow("All")
    val selectedDuaCategory: StateFlow<String> = _selectedDuaCategory.asStateFlow()

    // Observe DB & Preferences
    val bookmarks: StateFlow<List<BookmarkEntity>> = quranRepository.bookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<VerseNoteEntity>> = quranRepository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readingProgress = quranRepository.readingProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val dhikrProgressList = quranRepository.allDhikrProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Preferences
    val arabicFontSize = preferencesManager.arabicFontSize
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 28)

    val transFontSize = preferencesManager.transFontSize
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 15)

    val showEnglish = preferencesManager.showEnglish
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val showUrdu = preferencesManager.showUrdu
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val showSindhi = preferencesManager.showSindhi
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val arabicOnly = preferencesManager.arabicOnly
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val tajweedEnabled = preferencesManager.tajweedEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val wordByWordEnabled = preferencesManager.wordByWordEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val appTheme = preferencesManager.appTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "SYSTEM")

    val hapticEnabled = preferencesManager.hapticEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    // Audio Player state from Manager
    val audioState = audioManager.playerState

    // Filtered Verses
    val displayedVerses: StateFlow<List<Verse>> = _searchQuery
        .combine(MutableStateFlow(quranRepository.allVerses)) { query, all ->
            if (query.isBlank()) all else quranRepository.search(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), quranRepository.allVerses)

    // Filtered 99 Names
    val displayedAllahNames: StateFlow<List<AllahName>> = _namesSearchQuery
        .combine(MutableStateFlow(allahNamesList)) { query, all ->
            if (query.isBlank()) {
                all
            } else {
                val q = query.trim().lowercase()
                all.filter {
                    it.arabic.contains(q) ||
                    it.transliteration.lowercase().contains(q) ||
                    it.englishMeaning.lowercase().contains(q) ||
                    it.urduMeaning.contains(q) ||
                    it.number.toString() == q
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allahNamesList)

    // Filtered Duas
    val displayedDuas: StateFlow<List<DuaItem>> = _selectedDuaCategory
        .combine(MutableStateFlow(IslamicContentData.authenticDuas)) { cat, all ->
            if (cat == "All") all else all.filter { it.category.contains(cat, ignoreCase = true) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), IslamicContentData.authenticDuas)

    // Navigation
    fun navigateToTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun setToolsSubTab(subTab: ToolsSubTab) {
        _toolsSubTab.value = subTab
    }

    fun toggleDistractionFreeMode() {
        _isDistractionFreeMode.value = !_isDistractionFreeMode.value
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setNamesSearchQuery(query: String) {
        _namesSearchQuery.value = query
    }

    fun setSelectedDuaCategory(category: String) {
        _selectedDuaCategory.value = category
    }

    fun selectVerseForAction(verse: Verse?) {
        _selectedVerseForAction.value = verse
    }

    fun setSelectedCity(city: CityLocation) {
        _selectedCity.value = city
    }

    fun updateCompassAzimuth(azimuth: Float) {
        _compassAzimuth.value = azimuth
    }

    fun selectDhikr(dhikr: PredefinedDhikr) {
        _selectedDhikr.value = dhikr
        _customDhikrTarget.value = dhikr.defaultTarget
    }

    fun setCustomDhikrTarget(target: Int) {
        _customDhikrTarget.value = target
    }

    fun performHapticFeedback(isMilestone: Boolean = false) {
        if (!hapticEnabled.value) return
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = if (isMilestone) {
                    VibrationEffect.createWaveform(longArrayOf(0, 80, 80, 120), -1)
                } else {
                    VibrationEffect.createOneShot(35L, VibrationEffect.DEFAULT_AMPLITUDE)
                }
                vibrator?.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(if (isMilestone) 150L else 40L)
            }
        } catch (_: Exception) {}
    }

    fun incrementDhikrCount() {
        val currentDhikr = _selectedDhikr.value
        val progress = dhikrProgressList.value.find { it.dhikrId == currentDhikr.id }
        val currentCount = progress?.count ?: 0
        val cycles = progress?.completedCycles ?: 0
        val target = _customDhikrTarget.value

        val nextCount = currentCount + 1
        val isMilestone = nextCount >= target
        performHapticFeedback(isMilestone)

        viewModelScope.launch {
            quranRepository.incrementDhikr(currentDhikr.id, currentCount, target, cycles)
        }
    }

    fun resetDhikrCount() {
        performHapticFeedback(true)
        viewModelScope.launch {
            quranRepository.resetDhikr(_selectedDhikr.value.id)
        }
    }

    fun toggleBookmark(verseNumber: Int) {
        val isBookmarked = bookmarks.value.any { it.verseNumber == verseNumber }
        viewModelScope.launch {
            quranRepository.toggleBookmark(verseNumber, isBookmarked)
        }
    }

    fun saveVerseNote(verseNumber: Int, noteText: String) {
        viewModelScope.launch {
            quranRepository.saveNote(verseNumber, noteText)
        }
    }

    fun setLastReadVerse(verseNumber: Int) {
        viewModelScope.launch {
            quranRepository.updateReadingProgress(verseNumber)
        }
    }

    fun playVerseAudio(verseNumber: Int) {
        audioManager.seekToVerse(verseNumber)
        audioManager.play()
    }

    fun switchReciter(reciter: Reciter) {
        viewModelScope.launch {
            preferencesManager.setSelectedReciter(reciter.id)
            audioManager.switchReciter(reciter)
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        viewModelScope.launch {
            preferencesManager.setPlaybackSpeed(speed)
            audioManager.setPlaybackSpeed(speed)
        }
    }

    fun setRepeatMode(mode: String) {
        viewModelScope.launch {
            preferencesManager.setRepeatMode(mode)
            audioManager.setRepeatMode(mode)
        }
    }

    fun downloadSurahForReciter(reciter: Reciter) {
        audioManager.downloadSurah(reciter)
    }

    fun clearDownloadedAudio(reciterId: String) {
        audioManager.clearDownloadedAudio(reciterId)
    }

    fun resetAllSettings() {
        viewModelScope.launch {
            preferencesManager.resetSettings()
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}
