package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {

    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE verseNumber = :verseNumber)")
    fun isVerseBookmarked(verseNumber: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE verseNumber = :verseNumber")
    suspend fun deleteBookmarkByVerse(verseNumber: Int)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: Int)

    // Verse Notes
    @Query("SELECT * FROM verse_notes WHERE verseNumber = :verseNumber")
    fun getNoteForVerse(verseNumber: Int): Flow<VerseNoteEntity?>

    @Query("SELECT * FROM verse_notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<VerseNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(note: VerseNoteEntity)

    @Query("DELETE FROM verse_notes WHERE verseNumber = :verseNumber")
    suspend fun deleteNote(verseNumber: Int)

    // Reading Progress
    @Query("SELECT * FROM reading_progress WHERE id = 1")
    fun getReadingProgress(): Flow<ReadingProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReadingProgress(progress: ReadingProgressEntity)

    // Audio Downloads
    @Query("SELECT * FROM downloaded_verses WHERE reciterId = :reciterId")
    fun getDownloadedVersesForReciter(reciterId: String): Flow<List<DownloadedVerseEntity>>

    @Query("SELECT * FROM downloaded_verses")
    fun getAllDownloadedVerses(): Flow<List<DownloadedVerseEntity>>

    @Query("SELECT * FROM downloaded_verses WHERE reciterId = :reciterId AND verseNumber = :verseNumber")
    suspend fun getDownloadedVerse(reciterId: String, verseNumber: Int): DownloadedVerseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDownloadedVerse(entity: DownloadedVerseEntity)

    @Query("DELETE FROM downloaded_verses WHERE reciterId = :reciterId AND verseNumber = :verseNumber")
    suspend fun deleteDownloadedVerse(reciterId: String, verseNumber: Int)

    @Query("DELETE FROM downloaded_verses WHERE reciterId = :reciterId")
    suspend fun clearDownloadsForReciter(reciterId: String)
}

@Dao
interface DhikrDao {
    @Query("SELECT * FROM dhikr_progress")
    fun getAllDhikrProgress(): Flow<List<DhikrProgressEntity>>

    @Query("SELECT * FROM dhikr_progress WHERE dhikrId = :dhikrId")
    fun getDhikrProgress(dhikrId: String): Flow<DhikrProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDhikrProgress(entity: DhikrProgressEntity)

    @Query("UPDATE dhikr_progress SET count = 0, lastUpdated = :timestamp WHERE dhikrId = :dhikrId")
    suspend fun resetDhikr(dhikrId: String, timestamp: Long = System.currentTimeMillis())
}
