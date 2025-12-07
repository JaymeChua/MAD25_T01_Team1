package np.mad.assignment.mad_assignment_t01_team1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import np.mad.assignment.mad_assignment_t01_team1.data.entity.DishEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.ReviewEntity
@Dao
interface DishDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDish(dish: DishEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAllDishes(vararg dishes: DishEntity): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg dishes: DishEntity): List<Long>

    @Query("SELECT * FROM dishes WHERE stallId = :stallId ORDER BY dishName")
    fun getAllDishesForStall(stallId: Long): Flow<List<DishEntity>>
}