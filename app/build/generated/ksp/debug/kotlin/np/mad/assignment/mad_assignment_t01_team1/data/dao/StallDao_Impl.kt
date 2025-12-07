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
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `stalls` (`stallId`,`canteenName`,`canteenId`,`cuisine`,`description`,`name`,`imageResId`,`halal`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: StallEntity) {
        statement.bindLong(1, entity.stallId)
        statement.bindText(2, entity.canteenName)
        statement.bindLong(3, entity.canteenId)
        statement.bindText(4, entity.cuisine)
        statement.bindText(5, entity.description)
        statement.bindText(6, entity.name)
        statement.bindLong(7, entity.imageResId.toLong())
        val _tmp: Int = if (entity.halal) 1 else 0
        statement.bindLong(8, _tmp.toLong())
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
        val _columnIndexOfCanteenName: Int = getColumnIndexOrThrow(_stmt, "canteenName")
        val _columnIndexOfCanteenId: Int = getColumnIndexOrThrow(_stmt, "canteenId")
        val _columnIndexOfCuisine: Int = getColumnIndexOrThrow(_stmt, "cuisine")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfImageResId: Int = getColumnIndexOrThrow(_stmt, "imageResId")
        val _columnIndexOfHalal: Int = getColumnIndexOrThrow(_stmt, "halal")
        val _result: MutableList<StallEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StallEntity
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpCanteenName: String
          _tmpCanteenName = _stmt.getText(_columnIndexOfCanteenName)
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpCuisine: String
          _tmpCuisine = _stmt.getText(_columnIndexOfCuisine)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpImageResId: Int
          _tmpImageResId = _stmt.getLong(_columnIndexOfImageResId).toInt()
          val _tmpHalal: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfHalal).toInt()
          _tmpHalal = _tmp != 0
          _item = StallEntity(_tmpStallId,_tmpCanteenName,_tmpCanteenId,_tmpCuisine,_tmpDescription,_tmpName,_tmpImageResId,_tmpHalal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getByCanteenName(canteenName: String): Flow<List<StallEntity>> {
    val _sql: String = "SELECT * FROM stalls WHERE canteenName = ? ORDER BY name"
    return createFlow(__db, false, arrayOf("stalls")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, canteenName)
        val _columnIndexOfStallId: Int = getColumnIndexOrThrow(_stmt, "stallId")
        val _columnIndexOfCanteenName: Int = getColumnIndexOrThrow(_stmt, "canteenName")
        val _columnIndexOfCanteenId: Int = getColumnIndexOrThrow(_stmt, "canteenId")
        val _columnIndexOfCuisine: Int = getColumnIndexOrThrow(_stmt, "cuisine")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfImageResId: Int = getColumnIndexOrThrow(_stmt, "imageResId")
        val _columnIndexOfHalal: Int = getColumnIndexOrThrow(_stmt, "halal")
        val _result: MutableList<StallEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StallEntity
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpCanteenName: String
          _tmpCanteenName = _stmt.getText(_columnIndexOfCanteenName)
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpCuisine: String
          _tmpCuisine = _stmt.getText(_columnIndexOfCuisine)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpImageResId: Int
          _tmpImageResId = _stmt.getLong(_columnIndexOfImageResId).toInt()
          val _tmpHalal: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfHalal).toInt()
          _tmpHalal = _tmp != 0
          _item = StallEntity(_tmpStallId,_tmpCanteenName,_tmpCanteenId,_tmpCuisine,_tmpDescription,_tmpName,_tmpImageResId,_tmpHalal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getByIdFlow(stallId: Long): Flow<StallEntity?> {
    val _sql: String = "SELECT * FROM stalls WHERE stallId = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("stalls")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, stallId)
        val _columnIndexOfStallId: Int = getColumnIndexOrThrow(_stmt, "stallId")
        val _columnIndexOfCanteenName: Int = getColumnIndexOrThrow(_stmt, "canteenName")
        val _columnIndexOfCanteenId: Int = getColumnIndexOrThrow(_stmt, "canteenId")
        val _columnIndexOfCuisine: Int = getColumnIndexOrThrow(_stmt, "cuisine")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfImageResId: Int = getColumnIndexOrThrow(_stmt, "imageResId")
        val _columnIndexOfHalal: Int = getColumnIndexOrThrow(_stmt, "halal")
        val _result: StallEntity?
        if (_stmt.step()) {
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpCanteenName: String
          _tmpCanteenName = _stmt.getText(_columnIndexOfCanteenName)
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpCuisine: String
          _tmpCuisine = _stmt.getText(_columnIndexOfCuisine)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpImageResId: Int
          _tmpImageResId = _stmt.getLong(_columnIndexOfImageResId).toInt()
          val _tmpHalal: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfHalal).toInt()
          _tmpHalal = _tmp != 0
          _result = StallEntity(_tmpStallId,_tmpCanteenName,_tmpCanteenId,_tmpCuisine,_tmpDescription,_tmpName,_tmpImageResId,_tmpHalal)
        } else {
          _result = null
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
        val _columnIndexOfCanteenName: Int = getColumnIndexOrThrow(_stmt, "canteenName")
        val _columnIndexOfCanteenId: Int = getColumnIndexOrThrow(_stmt, "canteenId")
        val _columnIndexOfCuisine: Int = getColumnIndexOrThrow(_stmt, "cuisine")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfImageResId: Int = getColumnIndexOrThrow(_stmt, "imageResId")
        val _columnIndexOfHalal: Int = getColumnIndexOrThrow(_stmt, "halal")
        val _result: StallEntity?
        if (_stmt.step()) {
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpCanteenName: String
          _tmpCanteenName = _stmt.getText(_columnIndexOfCanteenName)
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpCuisine: String
          _tmpCuisine = _stmt.getText(_columnIndexOfCuisine)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpImageResId: Int
          _tmpImageResId = _stmt.getLong(_columnIndexOfImageResId).toInt()
          val _tmpHalal: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfHalal).toInt()
          _tmpHalal = _tmp != 0
          _result = StallEntity(_tmpStallId,_tmpCanteenName,_tmpCanteenId,_tmpCuisine,_tmpDescription,_tmpName,_tmpImageResId,_tmpHalal)
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
        val _columnIndexOfCanteenName: Int = getColumnIndexOrThrow(_stmt, "canteenName")
        val _columnIndexOfCanteenId: Int = getColumnIndexOrThrow(_stmt, "canteenId")
        val _columnIndexOfCuisine: Int = getColumnIndexOrThrow(_stmt, "cuisine")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfImageResId: Int = getColumnIndexOrThrow(_stmt, "imageResId")
        val _columnIndexOfHalal: Int = getColumnIndexOrThrow(_stmt, "halal")
        val _result: StallEntity?
        if (_stmt.step()) {
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpCanteenName: String
          _tmpCanteenName = _stmt.getText(_columnIndexOfCanteenName)
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpCuisine: String
          _tmpCuisine = _stmt.getText(_columnIndexOfCuisine)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpImageResId: Int
          _tmpImageResId = _stmt.getLong(_columnIndexOfImageResId).toInt()
          val _tmpHalal: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfHalal).toInt()
          _tmpHalal = _tmp != 0
          _result = StallEntity(_tmpStallId,_tmpCanteenName,_tmpCanteenId,_tmpCuisine,_tmpDescription,_tmpName,_tmpImageResId,_tmpHalal)
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
