package com.example.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val verseReference: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val verseReference: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "streaks")
data class StreakEntity(
    @PrimaryKey val id: Int = 1,
    val consecutiveDays: Int = 0,
    val lastReadTime: Long = 0,
    val consecutivePrayers: Int = 0,
    val lastPrayerTime: Long = 0,
    val consecutiveDevotionals: Int = 0,
    val lastDevotionalTime: Long = 0
)

@Entity(tableName = "prayer_requests")
data class PrayerRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val author: String,
    val timestamp: Long = System.currentTimeMillis(),
    val amenCount: Int = 0,
    val hasAmened: Boolean = false
)

@Entity(tableName = "unlocked_badges")
data class BadgeEntity(
    @PrimaryKey val id: String, // e.g. "STREAK_3", "PRAYER_WARRIOR"
    val badgeName: String,
    val description: String,
    val unlockedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_hymns")
data class FavoriteHymnEntity(
    @PrimaryKey val hymnId: Int, // id linked to preloaded hymns
    val language: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "READ", "DEVOTIONAL", "PRAYER", "QUIZ", "HYMN"
    val value: String,
    val timestamp: Long = System.currentTimeMillis()
)
