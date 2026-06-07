package com.example.database

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.io.Serializable

class BibleRepository(private val db: AppDatabase) {

    // DAOs
    val noteDao = db.noteDao()
    val bookmarkDao = db.bookmarkDao()
    val streakDao = db.streakDao()
    val prayerRequestDao = db.prayerRequestDao()
    val badgeDao = db.badgeDao()
    val favoriteHymnDao = db.favoriteHymnDao()
    val activityLogDao = db.activityLogDao()

    // Streaks Flow
    val streakFlow: Flow<StreakEntity?> = streakDao.getStreakFlow()
    val allBadges: Flow<List<BadgeEntity>> = badgeDao.getAllBadges()
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes()
    val allBookmarks: Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()
    val allPrayerRequests: Flow<List<PrayerRequestEntity>> = prayerRequestDao.getAllPrayerRequests()
    val favoriteHymns: Flow<List<FavoriteHymnEntity>> = favoriteHymnDao.getAllFavoriteHymns()
    val allActivityLogs: Flow<List<ActivityLogEntity>> = activityLogDao.getAllActivityLogs()

    // Initialize or get default streak
    private suspend fun getOrCreateStreak(): StreakEntity {
        val current = streakDao.getStreak()
        return current ?: StreakEntity().also { streakDao.insertOrUpdateStreak(it) }
    }

    // Record Reading & Update Streaks
    suspend fun recordReading(bookName: String, chapter: Int) {
        val now = System.currentTimeMillis()
        val todayNum = now / 86400000L

        val currentStreak = getOrCreateStreak()
        val lastReadNum = currentStreak.lastReadTime / 86400000L

        var newDays = currentStreak.consecutiveDays
        if (lastReadNum == 0L) {
            newDays = 1
        } else if (todayNum == lastReadNum + 1) {
            newDays += 1
        } else if (todayNum > lastReadNum + 1) {
            newDays = 1
        } // same day is ignored for streak increment, but updates timestamp

        val updated = currentStreak.copy(
            consecutiveDays = newDays,
            lastReadTime = now
        )
        streakDao.insertOrUpdateStreak(updated)

        // Log Activity
        activityLogDao.insertLog(ActivityLogEntity(type = "READ", value = "$bookName Chapter $chapter"))

        // Unlock Badge check
        badgeDao.unlockBadge(BadgeEntity("FIRST_STEP", "First Step", "Started your spiritual study journey!"))
        if (newDays >= 3) {
            badgeDao.unlockBadge(BadgeEntity("STREAK_3", "Faithful Reader", "Maintained a 3-day Scripture reading streak!"))
        }
    }

    // Record Devotional Study & Update Streaks
    suspend fun recordDevotional(title: String) {
        val now = System.currentTimeMillis()
        val todayNum = now / 86400000L

        val currentStreak = getOrCreateStreak()
        val lastDevNum = currentStreak.lastDevotionalTime / 86400000L

        var newDevStreak = currentStreak.consecutiveDevotionals
        if (lastDevNum == 0L) {
            newDevStreak = 1
        } else if (todayNum == lastDevNum + 1) {
            newDevStreak += 1
        } else if (todayNum > lastDevNum + 1) {
            newDevStreak = 1
        }

        val updated = currentStreak.copy(
            consecutiveDevotionals = newDevStreak,
            lastDevotionalTime = now
        )
        streakDao.insertOrUpdateStreak(updated)

        activityLogDao.insertLog(ActivityLogEntity(type = "DEVOTIONAL", value = "Completed: $title"))
        badgeDao.unlockBadge(BadgeEntity("DEVOUT_STUDENT", "Devout Student", "Completed a daily prayer devotional."))
    }

    // Record Prayer & Update Streaks
    suspend fun recordPrayer(prayerTitle: String) {
        val now = System.currentTimeMillis()
        val todayNum = now / 86400000L

        val currentStreak = getOrCreateStreak()
        val lastPrayerNum = currentStreak.lastPrayerTime / 86400000L

        var newPrayerStreak = currentStreak.consecutivePrayers
        if (lastPrayerNum == 0L) {
            newPrayerStreak = 1
        } else if (todayNum == lastPrayerNum + 1) {
            newPrayerStreak += 1
        } else if (todayNum > lastPrayerNum + 1) {
            newPrayerStreak = 1
        }

        val updated = currentStreak.copy(
            consecutivePrayers = newPrayerStreak,
            lastPrayerTime = now
        )
        streakDao.insertOrUpdateStreak(updated)

        activityLogDao.insertLog(ActivityLogEntity(type = "PRAYER", value = "Prayed: $prayerTitle"))
        badgeDao.unlockBadge(BadgeEntity("STEADFAST_PRAYER", "Prayer Shield", "Established consistent daily prayers."))
    }

    // Add Prayer Request to Community Wall
    suspend fun addPrayerRequest(title: String, description: String, author: String) {
        prayerRequestDao.insertPrayerRequest(
            PrayerRequestEntity(title = title, description = description, author = author)
        )
        activityLogDao.insertLog(ActivityLogEntity(type = "PRAYER", value = "Shared request: $title"))

        // Check if unlocked Prayer Warrior badge (based on shared requests count)
        val requests = prayerRequestDao.getAllPrayerRequests().firstOrNull() ?: emptyList()
        if (requests.size >= 2) {
            badgeDao.unlockBadge(BadgeEntity("PRAYER_WARRIOR", "Prayer Warrior", "Shared 3 or more community prayer needs!"))
        }
    }

    // Amen a Prayer Request
    suspend fun amenPrayerRequest(id: Int, currentCount: Int, alreadyAmened: Boolean) {
        if (!alreadyAmened) {
            prayerRequestDao.updateAmen(id, currentCount + 1, true)
            activityLogDao.insertLog(ActivityLogEntity(type = "PRAYER", value = "Said Amen to a prayer request"))
        } else {
            prayerRequestDao.updateAmen(id, (currentCount - 1).coerceAtLeast(0), false)
        }
    }

    // record quiz score
    suspend fun recordQuizScore(score: Int, total: Int, category: String) {
        activityLogDao.insertLog(ActivityLogEntity(type = "QUIZ", value = "Knowledge Quiz: $score/$total ($category)"))
        if (score == total) {
            badgeDao.unlockBadge(BadgeEntity("THEOLOGY_SCHOLAR", "Theology Scholar", "Scored 100% on a Biblical knowledge challenge!"))
        }
    }

    // Toggle Favorite Hymn
    suspend fun toggleFavoriteHymn(hymnId: Int, language: String) {
        val isFav = favoriteHymnDao.isFavorite(hymnId)
        if (isFav) {
            favoriteHymnDao.removeFavorite(hymnId)
        } else {
            favoriteHymnDao.addFavorite(FavoriteHymnEntity(hymnId = hymnId, language = language))
            activityLogDao.insertLog(ActivityLogEntity(type = "HYMN", value = "Favorited Hymn #$hymnId"))
            badgeDao.unlockBadge(BadgeEntity("WORSHIPPER", "Heart of Worship", "Favorited or offline-downloaded beautiful tribal hymns!"))
        }
    }
}
