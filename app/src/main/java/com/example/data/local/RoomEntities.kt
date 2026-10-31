package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val verseNumber: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
)

@Entity(tableName = "verse_notes")
data class VerseNoteEntity(
    @PrimaryKey
    val verseNumber: Int,
    val noteText: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_progress")
data class ReadingProgressEntity(
    @PrimaryKey
    val id: Int = 1,
    val lastReadVerse: Int = 1,
    val lastReadTimestamp: Long = System.currentTimeMillis(),
    val surahCompletedTimes: Int = 0
)

@Entity(tableName = "dhikr_progress")
data class DhikrProgressEntity(
    @PrimaryKey
    val dhikrId: String,
    val count: Int = 0,
    val targetCount: Int = 33,
    val completedCycles: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "downloaded_verses")
data class DownloadedVerseEntity(
    @PrimaryKey
    val id: String, // e.g. "alafasy_1"
    val reciterId: String,
    val verseNumber: Int,
    val filePath: String,
    val fileSize: Long
)
