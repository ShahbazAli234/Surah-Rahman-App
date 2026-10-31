package com.example

import com.example.data.islamic.IslamicContentData
import com.example.data.islamic.allahNamesList
import com.example.data.quran.SurahRahmanProvider
import com.example.data.repository.CityLocation
import com.example.data.repository.PrayerRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun surahArRahman_containsExactly78Verses() {
        val verses = SurahRahmanProvider.allVerses
        assertEquals("Surah Ar-Rahman must contain exactly 78 verses", 78, verses.size)
    }

    @Test
    fun surahArRahman_containsExactly31Refrains() {
        val refrains = SurahRahmanProvider.allVerses.filter { it.isRefrain }
        assertEquals("The refrain 'Fabi-ayyi ala-i Rabbikuma tukazziban' appears exactly 31 times", 31, refrains.size)
    }

    @Test
    fun surahArRahman_allVersesHaveValidTranslations() {
        val verses = SurahRahmanProvider.allVerses
        verses.forEach { verse ->
            assertTrue("Verse ${verse.verseNumber} Arabic text must not be empty", verse.arabicText.isNotBlank())
            assertTrue("Verse ${verse.verseNumber} English translation must not be empty", verse.englishTranslation.isNotBlank())
            assertTrue("Verse ${verse.verseNumber} Urdu translation must not be empty", verse.urduTranslation.isNotBlank())
            assertTrue("Verse ${verse.verseNumber} Sindhi translation must not be empty", verse.sindhiTranslation.isNotBlank())
            assertTrue("Verse ${verse.verseNumber} Transliteration must not be empty", verse.transliteration.isNotBlank())
        }
    }

    @Test
    fun surahArRahman_searchByNumberAndText() {
        val verse1 = SurahRahmanProvider.searchVerses("1")
        assertEquals(1, verse1.first().verseNumber)

        val englishSearch = SurahRahmanProvider.searchVerses("Merciful")
        assertTrue(englishSearch.isNotEmpty())

        val arabicSearch = SurahRahmanProvider.searchVerses("الرحمن")
        assertTrue(arabicSearch.isNotEmpty())
    }

    @Test
    fun namesOfAllah_contains99Names() {
        assertEquals("There must be exactly 99 Names of Allah", 99, allahNamesList.size)
        allahNamesList.forEach { name ->
            assertTrue(name.arabic.isNotBlank())
            assertTrue(name.englishMeaning.isNotBlank())
            assertTrue(name.urduMeaning.isNotBlank())
        }
    }

    @Test
    fun prayerSchedule_calculatesTimesCorrectly() {
        val repo = PrayerRepository()
        val makkah = CityLocation("Makkah", "Saudi Arabia", 21.4225, 39.8262, "Asia/Riyadh")
        val schedule = repo.getPrayerSchedule(makkah)

        assertNotNull(schedule.fajr)
        assertNotNull(schedule.dhuhr)
        assertNotNull(schedule.asr)
        assertNotNull(schedule.maghrib)
        assertNotNull(schedule.isha)

        val qibla = repo.calculateQiblaBearing(24.8607, 67.0011) // Karachi to Makkah
        assertTrue("Karachi Qibla bearing should be around 268°", qibla in 260.0f..275.0f)
    }

    @Test
    fun recitersAndDhikr_areValid() {
        val reciters = IslamicContentData.reciters
        assertEquals(3, reciters.size)

        val dhikr = IslamicContentData.predefinedDhikrList
        assertTrue(dhikr.size >= 5)

        val duas = IslamicContentData.authenticDuas
        assertTrue(duas.size >= 4)
    }
}
