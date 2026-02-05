package np.mad.assignment.mad_assignment_t01_team1.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

    @Query("SELECT * FROM users WHERE userId = :id")
    fun getUserById(id: Long): Flow<UserEntity?>

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>
}