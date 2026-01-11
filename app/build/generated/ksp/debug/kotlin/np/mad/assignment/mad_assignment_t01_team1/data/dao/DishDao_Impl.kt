package np.mad.assignment.mad_assignment_t01_team1.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
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
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import np.mad.assignment.mad_assignment_t01_team1.`data`.entity.DishEntity

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DishDao_Impl(
  __db: RoomDatabase,
) : DishDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDishEntity: EntityInsertAdapter<DishEntity>

  private val __deleteAdapterOfDishEntity: EntityDeleteOrUpdateAdapter<DishEntity>

  private val __updateAdapterOfDishEntity: EntityDeleteOrUpdateAdapter<DishEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfDishEntity = object : EntityInsertAdapter<DishEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `dishes` (`dishId`,`stallId`,`dishName`,`dishPrice`,`imageResId`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DishEntity) {
        statement.bindLong(1, entity.dishId)
        statement.bindLong(2, entity.stallId)
        statement.bindText(3, entity.dishName)
        statement.bindText(4, entity.dishPrice)
        statement.bindLong(5, entity.imageResId.toLong())
      }
    }
    this.__deleteAdapterOfDishEntity = object : EntityDeleteOrUpdateAdapter<DishEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `dishes` WHERE `dishId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DishEntity) {
        statement.bindLong(1, entity.dishId)
      }
    }
    this.__updateAdapterOfDishEntity = object : EntityDeleteOrUpdateAdapter<DishEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `dishes` SET `dishId` = ?,`stallId` = ?,`dishName` = ?,`dishPrice` = ?,`imageResId` = ? WHERE `dishId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DishEntity) {
        statement.bindLong(1, entity.dishId)
        statement.bindLong(2, entity.stallId)
        statement.bindText(3, entity.dishName)
        statement.bindText(4, entity.dishPrice)
        statement.bindLong(5, entity.imageResId.toLong())
        statement.bindLong(6, entity.dishId)
      }
    }
  }

  public override suspend fun addDish(dish: DishEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfDishEntity.insertAndReturnId(_connection, dish)
    _result
  }

  public override suspend fun addAllDishes(vararg dishes: DishEntity): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfDishEntity.insertAndReturnIdsList(_connection, dishes)
    _result
  }

  public override suspend fun insert(vararg dishes: DishEntity): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfDishEntity.insertAndReturnIdsList(_connection, dishes)
    _result
  }

  public override suspend fun deleteDish(dish: DishEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfDishEntity.handle(_connection, dish)
  }

  public override suspend fun updateDish(dish: DishEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfDishEntity.handle(_connection, dish)
  }

  public override fun getAllDishesForStall(stallId: Long): Flow<List<DishEntity>> {
    val _sql: String = "SELECT * FROM dishes WHERE stallId = ? ORDER BY dishName"
    return createFlow(__db, false, arrayOf("dishes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, stallId)
        val _columnIndexOfDishId: Int = getColumnIndexOrThrow(_stmt, "dishId")
        val _columnIndexOfStallId: Int = getColumnIndexOrThrow(_stmt, "stallId")
        val _columnIndexOfDishName: Int = getColumnIndexOrThrow(_stmt, "dishName")
        val _columnIndexOfDishPrice: Int = getColumnIndexOrThrow(_stmt, "dishPrice")
        val _columnIndexOfImageResId: Int = getColumnIndexOrThrow(_stmt, "imageResId")
        val _result: MutableList<DishEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DishEntity
          val _tmpDishId: Long
          _tmpDishId = _stmt.getLong(_columnIndexOfDishId)
          val _tmpStallId: Long
          _tmpStallId = _stmt.getLong(_columnIndexOfStallId)
          val _tmpDishName: String
          _tmpDishName = _stmt.getText(_columnIndexOfDishName)
          val _tmpDishPrice: String
          _tmpDishPrice = _stmt.getText(_columnIndexOfDishPrice)
          val _tmpImageResId: Int
          _tmpImageResId = _stmt.getLong(_columnIndexOfImageResId).toInt()
          _item = DishEntity(_tmpDishId,_tmpStallId,_tmpDishName,_tmpDishPrice,_tmpImageResId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteDishById(dishId: Long) {
    val _sql: String = "DELETE FROM dishes WHERE dishId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, dishId)
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
