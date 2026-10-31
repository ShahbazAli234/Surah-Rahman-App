package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "surah_rahman_settings")

class PreferencesManager(private val context: Context) {

    companion object {
        val KEY_ARABIC_FONT_SIZE = intPreferencesKey("arabic_font_size")
        val KEY_TRANS_FONT_SIZE = intPreferencesKey("trans_font_size")
        val KEY_SHOW_ENGLISH = booleanPreferencesKey("show_english")
        val KEY_SHOW_URDU = booleanPreferencesKey("show_urdu")
        val KEY_SHOW_SINDHI = booleanPreferencesKey("show_sindhi")
        val KEY_ARABIC_ONLY = booleanPreferencesKey("arabic_only")
        val KEY_TAJWEED = booleanPreferencesKey("tajweed_enabled")
        val KEY_WORD_BY_WORD = booleanPreferencesKey("word_by_word_enabled")
        val KEY_RECITER = stringPreferencesKey("selected_reciter")
        val KEY_PLAYBACK_SPEED = floatPreferencesKey("playback_speed")
        val KEY_REPEAT_MODE = stringPreferencesKey("repeat_mode")
        val KEY_APP_THEME = stringPreferencesKey("app_theme")
        val KEY_HAPTIC = booleanPreferencesKey("haptic_feedback")
    }

    val arabicFontSize: Flow<Int> = context.dataStore.data.map { it[KEY_ARABIC_FONT_SIZE] ?: 28 }
    val transFontSize: Flow<Int> = context.dataStore.data.map { it[KEY_TRANS_FONT_SIZE] ?: 15 }
    val showEnglish: Flow<Boolean> = context.dataStore.data.map { it[KEY_SHOW_ENGLISH] ?: true }
    val showUrdu: Flow<Boolean> = context.dataStore.data.map { it[KEY_SHOW_URDU] ?: true }
    val showSindhi: Flow<Boolean> = context.dataStore.data.map { it[KEY_SHOW_SINDHI] ?: false }
    val arabicOnly: Flow<Boolean> = context.dataStore.data.map { it[KEY_ARABIC_ONLY] ?: false }
    val tajweedEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_TAJWEED] ?: true }
    val wordByWordEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_WORD_BY_WORD] ?: false }
    val selectedReciter: Flow<String> = context.dataStore.data.map { it[KEY_RECITER] ?: "alafasy" }
    val playbackSpeed: Flow<Float> = context.dataStore.data.map { it[KEY_PLAYBACK_SPEED] ?: 1.0f }
    val repeatMode: Flow<String> = context.dataStore.data.map { it[KEY_REPEAT_MODE] ?: "SURAH" }
    val appTheme: Flow<String> = context.dataStore.data.map { it[KEY_APP_THEME] ?: "SYSTEM" }
    val hapticEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_HAPTIC] ?: true }

    suspend fun setArabicFontSize(size: Int) {
        context.dataStore.edit { it[KEY_ARABIC_FONT_SIZE] = size }
    }

    suspend fun setTransFontSize(size: Int) {
        context.dataStore.edit { it[KEY_TRANS_FONT_SIZE] = size }
    }

    suspend fun setShowEnglish(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_ENGLISH] = show }
    }

    suspend fun setShowUrdu(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_URDU] = show }
    }

    suspend fun setShowSindhi(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_SINDHI] = show }
    }

    suspend fun setArabicOnly(enabled: Boolean) {
        context.dataStore.edit { it[KEY_ARABIC_ONLY] = enabled }
    }

    suspend fun setTajweed(enabled: Boolean) {
        context.dataStore.edit { it[KEY_TAJWEED] = enabled }
    }

    suspend fun setWordByWord(enabled: Boolean) {
        context.dataStore.edit { it[KEY_WORD_BY_WORD] = enabled }
    }

    suspend fun setSelectedReciter(reciterId: String) {
        context.dataStore.edit { it[KEY_RECITER] = reciterId }
    }

    suspend fun setPlaybackSpeed(speed: Float) {
        context.dataStore.edit { it[KEY_PLAYBACK_SPEED] = speed }
    }

    suspend fun setRepeatMode(mode: String) {
        context.dataStore.edit { it[KEY_REPEAT_MODE] = mode }
    }

    suspend fun setAppTheme(theme: String) {
        context.dataStore.edit { it[KEY_APP_THEME] = theme }
    }

    suspend fun setHaptic(enabled: Boolean) {
        context.dataStore.edit { it[KEY_HAPTIC] = enabled }
    }

    suspend fun resetSettings() {
        context.dataStore.edit {
            it.clear()
        }
    }
}
