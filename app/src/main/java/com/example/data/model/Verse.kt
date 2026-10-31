package com.example.data.model

data class WordMeaning(
    val arabic: String,
    val transliteration: String,
    val english: String,
    val urdu: String
)

data class Verse(
    val verseNumber: Int,
    val arabicText: String,
    val englishTranslation: String,
    val urduTranslation: String,
    val sindhiTranslation: String,
    val transliteration: String,
    val keyWords: List<WordMeaning> = emptyList(),
    val isRefrain: Boolean = false
)

data class SurahInfo(
    val number: Int = 55,
    val arabicName: String = "الرحمن",
    val englishName: String = "Ar-Rahman",
    val titleMeaning: String = "The Most Merciful",
    val totalVerses: Int = 78,
    val revelationType: String = "Madani / Makki (Authoritative consensus)",
    val totalRukus: Int = 3,
    val juzNumber: Int = 27,
    val refrainCount: Int = 31,
    val benefits: String = "Surah Ar-Rahman is known as the 'Beauty/Bride of the Quran' (عروس القرآن). It enumerates Allah's countless blessings—from the creation of mankind, sun, moon, oceans, and stars to the descriptions of Paradise—reminding both humans and jinn 31 times to reflect upon and never deny Allah's divine favors."
)
