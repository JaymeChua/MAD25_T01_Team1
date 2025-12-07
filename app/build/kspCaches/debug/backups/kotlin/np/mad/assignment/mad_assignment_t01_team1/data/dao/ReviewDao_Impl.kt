package np.mad.assignment.mad_assignment_t01_team1.`data`.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import np.mad.assignment.mad_assignment_t01_team1.`data`.entity.ReviewEntity

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ReviewDao_Impl(
  __db: RoomDatabase,
) : ReviewDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfReviewEntity: EntityInsertAdapter<ReviewEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfReviewEntity = object : EntityInsertAdapter<ReviewEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `reviews` (`reviewId`,`stallId`,`userId`,`username`,`review`,`rating`,`date`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ReviewEntity) {
        statement.bindLong(1, entity.reviewId)
        statement.bindLong(2, entity.stallId)
        statement.bindLong(3, entity.userId)
        statement.bindText(4, entity.username)
        statement.bindText(5, entity.review)
        statement.bindLong(6, entity.rating.toLong())
        statement.bindText(7, entity.date)
      }
    }
  }

  public override suspend fun addReview(review: ReviewEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfReviewEntity.insertAndReturnId(_connection, review)
    _result
  }

  public override suspend fun addReviews(vararg reviews: ReviewEntity): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfReviewEntity.insertAndReturnIdsList(_connection, reviews)
    _result
  }

  public override suspend fun insert(vararg reviews: ReviewEntity): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfReviewEntity.insertAndReturnIdsList(_connection, reviews)
    _result
  }

  public override fun getAllReviewsForStall(stallId: Long): Flow<List<ReviewEntity>> {
    val _sql: String = "SELECT * FROM reviews WHERE stallId = ? ORDER BY date"
    return createFlow(__db, false, arrayOf("reviews")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, stallId)
        val _columnIndexOfReviewId: Int = getColumnIndexOrThrow(_stmt, "reviewId")
        val _columnIndexOfStallId: Int = getColumnIndexOrThrow(_stmt, "stallId")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfUsername: Int = getColumnIndexOrThrow(_stmt, "username")
        val _columnIndexOfReview: Int = getColumnIndexOrThrow(_stmt, "review")
        val _columnIndexOfRating: Int = getColumnIndexOrThrow(_stmt, "rating")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _result: MutableList<ReviewEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReviewEntity
          val _tmpReviewId: Long
          _tmpReviewId = _stmt.getLong(_columnIndexOfReviewId)
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpUsername: String
          _tmpUsername = _stmt.getText(_columnIndexOfUsername)
          val _tmpReview: String
          _tmpReview = _stmt.getText(_columnIndexOfReview)
          val _tmpRating: Int
          _tmpRating = _stmt.getLong(_columnIndexOfRating).toInt()
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          _item = ReviewEntity(_tmpReviewId,_tmpStallId,_tmpUserId,_tmpUsername,_tmpReview,_tmpRating,_tmpDate)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
