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
import np.mad.assignment.mad_assignment_t01_team1.`data`.entity.CanteenEntity

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CanteenDao_Impl(
  __db: RoomDatabase,
) : CanteenDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCanteenEntity: EntityInsertAdapter<CanteenEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfCanteenEntity = object : EntityInsertAdapter<CanteenEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `canteens` (`canteenId`,`name`) VALUES (nullif(?, 0),?)"

      protected override fun bind(statement: SQLiteStatement, entity: CanteenEntity) {
        statement.bindLong(1, entity.canteenId)
        statement.bindText(2, entity.name)
      }
    }
  }

  public override suspend fun insert(vararg canteens: CanteenEntity): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfCanteenEntity.insertAndReturnIdsList(_connection, canteens)
    _result
  }

  public override fun getALL(): Flow<List<CanteenEntity>> {
    val _sql: String = "SELECT * FROM canteens ORDER BY name"
    return createFlow(__db, false, arrayOf("canteens")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfCanteenId: Int = getColumnIndexOrThrow(_stmt, "canteenId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: MutableList<CanteenEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CanteenEntity
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          _item = CanteenEntity(_tmpCanteenId,_tmpName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getByName(name: String): CanteenEntity? {
    val _sql: String = "SELECT * FROM canteens WHERE name = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, name)
        val _columnIndexOfCanteenId: Int = getColumnIndexOrThrow(_stmt, "canteenId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: CanteenEntity?
        if (_stmt.step()) {
          val _tmpCanteenId: Long
          _tmpCanteenId = _stmt.getLong(_columnIndexOfCanteenId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          _result = CanteenEntity(_tmpCanteenId,_tmpName)
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
