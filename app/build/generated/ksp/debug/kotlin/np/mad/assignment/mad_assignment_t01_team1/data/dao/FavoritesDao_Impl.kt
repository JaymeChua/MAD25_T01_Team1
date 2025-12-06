package np.mad.assignment.mad_assignment_t01_team1.`data`.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import np.mad.assignment.mad_assignment_t01_team1.`data`.entity.FavoriteEntity
import np.mad.assignment.mad_assignment_t01_team1.model.FavoriteStallUi

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FavoritesDao_Impl(
  __db: RoomDatabase,
) : FavoritesDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFavoriteEntity: EntityInsertAdapter<FavoriteEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfFavoriteEntity = object : EntityInsertAdapter<FavoriteEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `favorites` (`favoriteId`,`userId`,`stallId`) VALUES (nullif(?, 0),?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FavoriteEntity) {
        statement.bindLong(1, entity.favoriteId)
        statement.bindLong(2, entity.userId)
        statement.bindLong(3, entity.stallId)
      }
    }
  }

  public override suspend fun addFavorite(fav: FavoriteEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfFavoriteEntity.insertAndReturnId(_connection, fav)
    _result
  }

  public override suspend fun addFavorites(vararg favs: FavoriteEntity): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfFavoriteEntity.insertAndReturnIdsList(_connection, favs)
    _result
  }

  public override fun getFavoriteStallsForUser(userId: Long): Flow<List<FavoriteStallUi>> {
    val _sql: String = """
        |
        |        SELECT f.favoriteId,
        |                s.stallId,
        |                s.name AS stallName,
        |                s.imageUrl,
        |                s.halal,
        |                c.canteenId,
        |                c.name AS canteenName
        |                FROM favorites f
        |                INNER JOIN stalls s ON s.stallId = f.stallId
        |                INNER JOIN canteens c ON c.canteenId =s.canteenId
        |                WHERE f.userId = ?
        |                ORDER BY c.name, s.name
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("favorites", "stalls", "canteens")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        val _columnIndexOfFavoriteId: Int = 0
        val _columnIndexOfStallId: Int = 1
        val _columnIndexOfStallName: Int = 2
        val _columnIndexOfImageUrl: Int = 3
        val _columnIndexOfHalal: Int = 4
        val _columnIndexOfCanteenId: Int = 5
        val _columnIndexOfCanteenName: Int = 6
        val _result: MutableList<FavoriteStallUi> = mutableListOf()
        while (_stmt.step()) {
          val _item: FavoriteStallUi
          val _tmpFavoriteId: Long
          _tmpFavoriteId = _stmt.getLong(_columnIndexOfFavoriteId)
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpStallName: String
          _tmpStallName = _stmt.getText(_columnIndexOfStallName)
          val _tmpImageUrl: String?
          if (_stmt.isNull(_columnIndexOfImageUrl)) {
            _tmpImageUrl = null
          } else {
            _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          }
          val _tmpHalal: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfHalal).toInt()
          _tmpHalal = _tmp != 0
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpCanteenName: String
          _tmpCanteenName = _stmt.getText(_columnIndexOfCanteenName)
          _item = FavoriteStallUi(_tmpFavoriteId,_tmpStallId,_tmpStallName,_tmpCanteenId,_tmpCanteenName,_tmpImageUrl,_tmpHalal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun removeFavoriteById(favoriteId: Long) {
    val _sql: String = "DELETE FROM favorites WHERE favoriteId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, favoriteId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun removeFavoriteByUserAndStall(userId: Long, stallId: Long) {
    val _sql: String = "DELETE FROM favorites WHERE userId = ? AND stallId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, stallId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
