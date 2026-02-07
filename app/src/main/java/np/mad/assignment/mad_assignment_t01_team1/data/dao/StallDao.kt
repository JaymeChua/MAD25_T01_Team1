package np.mad.assignment.mad_assignment_t01_team1.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity

@Dao
interface StallDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg stalls: StallEntity): List<Long>

    @Query("SELECT * FROM stalls WHERE canteenId = :canteenId ORDER BY name")
    fun getByCanteen(canteenId: Long): Flow<List<StallEntity>>

    @Query("SELECT * FROM stalls WHERE canteenName = :canteenName ORDER BY name")
    fun getByCanteenName(canteenName: String): Flow<List<StallEntity>>

    @Query("SELECT * FROM stalls WHERE stallId = :stallId LIMIT 1")
    fun getByIdFlow(stallId: Long): Flow<StallEntity?>

    @Query("SELECT  * FROM stalls WHERE stallId = :stallId LIMIT 1")
    suspend fun getById(stallId: Long): StallEntity?

    @Query("SELECT * FROM stalls WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): StallEntity?

    @Query("""
    SELECT s.*, COUNT(r.reviewId) as review_count 
    FROM stalls s 
    LEFT JOIN reviews r ON s.stallId = r.stallId 
    GROUP BY s.stallId 
    ORDER BY review_count DESC
""")
    fun getStallsOrderedByPopularity(): Flow<List<StallEntity>>

    @Query("SELECT DISTINCT cuisine FROM stalls")
    fun getAllCuisines(): Flow<List<String>>

    @Update
    suspend fun updateStall(stall: StallEntity)

    @Delete
    suspend fun deleteStall(stall: StallEntity)
    @Query("SELECT * FROM stalls")
    suspend fun getAllStallsNow(): List<StallEntity>
    @Query("DELETE FROM stalls WHERE stallId = :stallId")
    suspend fun deleteStallById(stallId: Long)
}