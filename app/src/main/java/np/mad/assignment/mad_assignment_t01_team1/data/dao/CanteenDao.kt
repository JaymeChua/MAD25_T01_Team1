package np.mad.assignment.mad_assignment_t01_team1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import np.mad.assignment.mad_assignment_t01_team1.data.entity.CanteenEntity

@Dao
interface CanteenDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg canteens: CanteenEntity): List<Long>

    @Query("SELECT * FROM canteens ORDER BY name")
    fun getALL(): Flow<List<CanteenEntity>>

    @Query("SELECT * FROM canteens WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): CanteenEntity?

    @Query("SELECT * FROM canteens")
    suspend fun getAllNow(): List<CanteenEntity>

//    @Query("SELECT * FROM canteens WHERE remoteId = :remoteId LIMIT 1")
//    suspend fun getByRemoteId(remoteId: String): CanteenEntity?

}