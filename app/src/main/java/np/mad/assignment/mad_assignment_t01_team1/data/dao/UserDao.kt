package np.mad.assignment.mad_assignment_t01_team1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import np.mad.assignment.mad_assignment_t01_team1.data.entity.UserEntity

@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    fun getById(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): UserEntity?

    @Query("DELETE FROM users WHERE userId = :userId")
    suspend fun deleteUser(userId: Long)

}