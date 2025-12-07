package np.mad.assignment.mad_assignment_t01_team1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import np.mad.assignment.mad_assignment_t01_team1.data.entity.FavoriteEntity
import np.mad.assignment.mad_assignment_t01_team1.model.FavoriteStallUi

@Dao
interface FavoritesDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(fav: FavoriteEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorites(vararg favs: FavoriteEntity): List<Long>

    @Query("DELETE FROM favorites WHERE favoriteId = :favoriteId")
    suspend fun removeFavoriteById(favoriteId: Long)

    @Query("DELETE FROM favorites WHERE userId = :userId AND stallId = :stallId")
    suspend fun  removeFavoriteByUserAndStall(userId: Long,stallId: Long)

    @Query("""
        SELECT f.favoriteId,
                s.stallId,
                s.name AS stallName,
                s.imageResId,
                s.halal,
                c.canteenId,
                c.name AS canteenName
                FROM favorites f
                INNER JOIN stalls s ON s.stallId = f.stallId
                INNER JOIN canteens c ON c.canteenId =s.canteenId
                WHERE f.userId = :userId
                ORDER BY c.name, s.name
    """)
    fun getFavoriteStallsForUser(userId: Long): Flow<List<FavoriteStallUi>>
    @Query("SELECT stallId FROM favorites WHERE userId = :userId")
    fun getFavoriteStallIdsForUser(userId: Long): Flow<List<Long>>
}
