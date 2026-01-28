package np.mad.assignment.mad_assignment_t01_team1.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import np.mad.assignment.mad_assignment_t01_team1.data.entity.ReviewEntity

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReview(review: ReviewEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReviews(vararg reviews: ReviewEntity): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg reviews: ReviewEntity): List<Long>

    @Query("SELECT * FROM reviews WHERE stallId = :stallId ORDER BY date")
    fun getAllReviewsForStall(stallId: Long): Flow<List<ReviewEntity>>

    @Query("SELECT COUNT(*) FROM reviews WHERE userId = :userId")
    fun getReviewCountForUser(userId: Long): Flow<Int>

    @Update
    suspend fun updateReview(review: ReviewEntity)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)

    @Query("DELETE FROM reviews WHERE reviewId = :reviewId")
    suspend fun deleteReviewById(reviewId: Long)
}