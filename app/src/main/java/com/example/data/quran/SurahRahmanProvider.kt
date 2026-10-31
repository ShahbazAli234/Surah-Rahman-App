package com.example.data.quran

import com.example.data.model.SurahInfo
import com.example.data.model.Verse
import java.util.Calendar

object SurahRahmanProvider {

    const val BISMILLAH_ARABIC = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
    const val BISMILLAH_ENGLISH = "In the name of Allah, the Entirely Merciful, the Especially Merciful"
    const val BISMILLAH_URDU = "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے"
    const val BISMILLAH_SINDHI = "شروع الله جي نالي سان جيڪو وڏو مھربان نھايت رحم وارو آھي"

    val surahInfo = SurahInfo()

    val allVerses: List<Verse> by lazy {
        versesPart1 + versesPart2 + versesPart3
    }

    fun getVerse(verseNumber: Int): Verse? {
        return allVerses.getOrNull(verseNumber - 1)
    }

    fun getDailyVerse(): Verse {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1) % allVerses.size
        return allVerses[index]
    }

    fun normalizeArabic(text: String): String {
        return text.replace(Regex("[\u064B-\u065F\u0670\u06D6-\u06ED]"), "")
            .replace("أ", "ا")
            .replace("إ", "ا")
            .replace("آ", "ا")
            .replace("ٱ", "ا")
            .replace("ة", "ه")
            .replace("ى", "ي")
    }

    fun searchVerses(query: String): List<Verse> {
        val cleanQuery = query.trim().lowercase()
        if (cleanQuery.isEmpty()) return allVerses

        // If query is a number
        cleanQuery.toIntOrNull()?.let { number ->
            getVerse(number)?.let { return listOf(it) }
        }

        val normQuery = normalizeArabic(cleanQuery)

        return allVerses.filter { verse ->
            verse.verseNumber.toString() == cleanQuery ||
            verse.arabicText.contains(cleanQuery, ignoreCase = true) ||
            normalizeArabic(verse.arabicText).contains(normQuery, ignoreCase = true) ||
            verse.englishTranslation.contains(cleanQuery, ignoreCase = true) ||
            verse.urduTranslation.contains(cleanQuery, ignoreCase = true) ||
            normalizeArabic(verse.urduTranslation).contains(normQuery, ignoreCase = true) ||
            verse.sindhiTranslation.contains(cleanQuery, ignoreCase = true) ||
            verse.transliteration.contains(cleanQuery, ignoreCase = true)
        }
    }
}
