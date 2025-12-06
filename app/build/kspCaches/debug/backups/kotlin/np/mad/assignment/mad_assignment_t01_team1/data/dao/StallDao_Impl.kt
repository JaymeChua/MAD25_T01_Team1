package np.mad.assignment.mad_assignment_t01_team1.`data`.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
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
import np.mad.assignment.mad_assignment_t01_team1.`data`.entity.StallEntity

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class StallDao_Impl(
  __db: RoomDatabase,
) : StallDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfStallEntity: EntityInsertAdapter<StallEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfStallEntity = object : EntityInsertAdapter<StallEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `stalls` (`stallId`,`canteenId`,`name`,`imageUrl`,`halal`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: StallEntity) {
        statement.bindLong(1, entity.stallId)
        statement.bindLong(2, entity.canteenId)
        statement.bindText(3, entity.name)
        val _tmpImageUrl: String? = entity.imageUrl
        if (_tmpImageUrl == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpImageUrl)
        }
        val _tmp: Int = if (entity.halal) 1 else 0
        statement.bindLong(5, _tmp.toLong())
      }
    }
  }

  public override suspend fun insert(vararg stalls: StallEntity): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfStallEntity.insertAndReturnIdsList(_connection, stalls)
    _result
  }

  public override fun getByCanteen(canteenId: Long): Flow<List<StallEntity>> {
    val _sql: String = "SELECT * FROM stalls WHERE canteenId = ? ORDER BY name"
    return createFlow(__db, false, arrayOf("stalls")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, canteenId)
        val _columnIndexOfStallId: Int = getColumnIndexOrThrow(_stmt, "stallId")
        val _columnIndexOfCanteenId: Int = getColumnIndexOrThrow(_stmt, "canteenId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfHalal: Int = getColumnIndexOrThrow(_stmt, "halal")
        val _result: MutableList<StallEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StallEntity
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
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
          _item = StallEntity(_tmpStallId,_tmpCanteenId,_tmpName,_tmpImageUrl,_tmpHalal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(stallId: Long): StallEntity? {
    val _sql: String = "SELECT  * FROM stalls WHERE stallId = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, stallId)
        val _columnIndexOfStallId: Int = getColumnIndexOrThrow(_stmt, "stallId")
        val _columnIndexOfCanteenId: Int = getColumnIndexOrThrow(_stmt, "canteenId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfHalal: Int = getColumnIndexOrThrow(_stmt, "halal")
        val _result: StallEntity?
        if (_stmt.step()) {
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
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
          _result = StallEntity(_tmpStallId,_tmpCanteenId,_tmpName,_tmpImageUrl,_tmpHalal)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getByName(name: String): StallEntity? {
    val _sql: String = "SELECT * FROM stalls WHERE name = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, name)
        val _columnIndexOfStallId: Int = getColumnIndexOrThrow(_stmt, "stallId")
        val _columnIndexOfCanteenId: Int = getColumnIndexOrThrow(_stmt, "canteenId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfHalal: Int = getColumnIndexOrThrow(_stmt, "halal")
        val _result: StallEntity?
        if (_stmt.step()) {
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
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
          _result = StallEntity(_tmpStallId,_tmpCanteenId,_tmpName,_tmpImageUrl,_tmpHalal)
        } else {
          _result = null
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
