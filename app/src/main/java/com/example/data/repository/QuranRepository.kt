package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.BookmarkEntity
import com.example.data.local.DhikrProgressEntity
import com.example.data.local.DownloadedVerseEntity
import com.example.data.local.ReadingProgressEntity
import com.example.data.local.VerseNoteEntity
import com.example.data.model.SurahInfo
import com.example.data.model.Verse
import com.example.data.quran.SurahRahmanProvider
import kotlinx.coroutines.flow.Flow

class QuranRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val quranDao = db.quranDao()
    private val dhikrDao = db.dhikrDao()

    val allVerses: List<Verse> = SurahRahmanProvider.allVerses
    val surahInfo: SurahInfo = SurahRahmanProvider.surahInfo

    val bookmarks: Flow<List<BookmarkEntity>> = quranDao.getAllBookmarks()
    val allNotes: Flow<List<VerseNoteEntity>> = quranDao.getAllNotes()
    val readingProgress: Flow<ReadingProgressEntity?> = quranDao.getReadingProgress()
    val allDhikrProgress: Flow<List<DhikrProgressEntity>> = dhikrDao.getAllDhikrProgress()
    val downloadedVerses: Flow<List<DownloadedVerseEntity>> = quranDao.getAllDownloadedVerses()

    fun getVerse(number: Int): Verse? = SurahRahmanProvider.getVerse(number)

    fun getDailyVerse(): Verse = SurahRahmanProvider.getDailyVerse()

    fun search(query: String): List<Verse> = SurahRahmanProvider.searchVerses(query)

    fun isVerseBookmarked(verseNumber: Int): Flow<Boolean> = quranDao.isVerseBookmarked(verseNumber)

    suspend fun toggleBookmark(verseNumber: Int, isBookmarked: Boolean, note: String = "") {
        if (isBookmarked) {
            quranDao.deleteBookmarkByVerse(verseNumber)
        } else {
            quranDao.insertBookmark(BookmarkEntity(verseNumber = verseNumber, note = note))
        }
    }

    suspend fun removeBookmarkById(id: Int) {
        quranDao.deleteBookmarkById(id)
    }

    fun getNoteForVerse(verseNumber: Int): Flow<VerseNoteEntity?> = quranDao.getNoteForVerse(verseNumber)

    suspend fun saveNote(verseNumber: Int, noteText: String) {
        if (noteText.isBlank()) {
            quranDao.deleteNote(verseNumber)
        } else {
            quranDao.saveNote(VerseNoteEntity(verseNumber = verseNumber, noteText = noteText.trim()))
        }
    }

    suspend fun updateReadingProgress(lastReadVerse: Int) {
        quranDao.updateReadingProgress(
            ReadingProgressEntity(
                id = 1,
                lastReadVerse = lastReadVerse,
                lastReadTimestamp = System.currentTimeMillis()
            )
        )
    }

    fun getDhikrProgress(dhikrId: String): Flow<DhikrProgressEntity?> = dhikrDao.getDhikrProgress(dhikrId)

    suspend fun incrementDhikr(dhikrId: String, currentCount: Int, target: Int, cycles: Int) {
        val nextCount = currentCount + 1
        if (nextCount >= target) {
            dhikrDao.saveDhikrProgress(
                DhikrProgressEntity(
                    dhikrId = dhikrId,
                    count = 0,
                    targetCount = target,
                    completedCycles = cycles + 1,
                    lastUpdated = System.currentTimeMillis()
                )
            )
        } else {
            dhikrDao.saveDhikrProgress(
                DhikrProgressEntity(
                    dhikrId = dhikrId,
                    count = nextCount,
                    targetCount = target,
                    completedCycles = cycles,
                    lastUpdated = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun resetDhikr(dhikrId: String) {
        dhikrDao.resetDhikr(dhikrId)
    }

    suspend fun recordDownloadedVerse(reciterId: String, verseNumber: Int, filePath: String, size: Long) {
        quranDao.saveDownloadedVerse(
            DownloadedVerseEntity(
                id = "${reciterId}_$verseNumber",
                reciterId = reciterId,
                verseNumber = verseNumber,
                filePath = filePath,
                fileSize = size
            )
        )
    }

    suspend fun removeDownloadedVerse(reciterId: String, verseNumber: Int) {
        quranDao.deleteDownloadedVerse(reciterId, verseNumber)
    }

    suspend fun clearDownloadsForReciter(reciterId: String) {
        quranDao.clearDownloadsForReciter(reciterId)
    }
}
