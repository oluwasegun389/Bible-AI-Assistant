package com.example.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Int)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE verseReference = :ref")
    suspend fun deleteBookmark(ref: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE verseReference = :ref)")
    suspend fun isBookmarked(ref: String): Boolean
}

@Dao
interface StreakDao {
    @Query("SELECT * FROM streaks WHERE id = 1")
    fun getStreakFlow(): Flow<StreakEntity?>

    @Query("SELECT * FROM streaks WHERE id = 1")
    suspend fun getStreak(): StreakEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStreak(streak: StreakEntity)
}

@Dao
interface PrayerRequestDao {
    @Query("SELECT * FROM prayer_requests ORDER BY timestamp DESC")
    fun getAllPrayerRequests(): Flow<List<PrayerRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerRequest(request: PrayerRequestEntity)

    @Query("UPDATE prayer_requests SET amenCount = :count, hasAmened = :hasAmened WHERE id = :id")
    suspend fun updateAmen(id: Int, count: Int, hasAmened: Boolean)

    @Query("DELETE FROM prayer_requests WHERE id = :id")
    suspend fun deletePrayerRequest(id: Int)
}

@Dao
interface BadgeDao {
    @Query("SELECT * FROM unlocked_badges")
    fun getAllBadges(): Flow<List<BadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockBadge(badge: BadgeEntity)
}

@Dao
interface FavoriteHymnDao {
    @Query("SELECT * FROM favorite_hymns")
    fun getAllFavoriteHymns(): Flow<List<FavoriteHymnEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(hymn: FavoriteHymnEntity)

    @Query("DELETE FROM favorite_hymns WHERE hymnId = :hymnId")
    suspend fun removeFavorite(hymnId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_hymns WHERE hymnId = :hymnId)")
    suspend fun isFavorite(hymnId: Int): Boolean
}

@Dao
interface ActivityLogDao {
    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC")
    fun getAllActivityLogs(): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ActivityLogEntity)

    @Query("SELECT COUNT(*) FROM activity_logs WHERE type = 'READ'")
    suspend fun getReadCount(): Int

    @Query("SELECT COUNT(*) FROM activity_logs WHERE type = 'QUIZ'")
    suspend fun getQuizCount(): Int
}
