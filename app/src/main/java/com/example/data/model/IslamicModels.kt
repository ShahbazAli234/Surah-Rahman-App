package com.example.data.model

data class Reciter(
    val id: String,
    val name: String,
    val arabicName: String,
    val style: String,
    val audioBaseUrl: String,
    val fullSurahUrl: String
)

data class PrayerSchedule(
    val cityName: String,
    val dateString: String,
    val hijriDateString: String,
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val nextPrayerName: String,
    val nextPrayerTime: String,
    val remainingTimeFormatted: String
)

data class AllahName(
    val number: Int,
    val arabic: String,
    val transliteration: String,
    val englishMeaning: String,
    val urduMeaning: String,
    val explanation: String
)

data class IslamicEvent(
    val hijriDate: String,
    val title: String,
    val arabicTitle: String,
    val description: String,
    val isUpcoming: Boolean = false
)

data class DuaItem(
    val id: String,
    val category: String,
    val title: String,
    val arabic: String,
    val transliteration: String,
    val translationEnglish: String,
    val translationUrdu: String,
    val reference: String
)

data class PredefinedDhikr(
    val id: String,
    val arabic: String,
    val transliteration: String,
    val englishMeaning: String,
    val urduMeaning: String,
    val defaultTarget: Int = 33,
    val reward: String
)
