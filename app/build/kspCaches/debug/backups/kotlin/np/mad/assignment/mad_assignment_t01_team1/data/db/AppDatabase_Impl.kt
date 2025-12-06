package np.mad.assignment.mad_assignment_t01_team1.`data`.db

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass
import np.mad.assignment.mad_assignment_t01_team1.`data`.dao.CanteenDao
import np.mad.assignment.mad_assignment_t01_team1.`data`.dao.CanteenDao_Impl
import np.mad.assignment.mad_assignment_t01_team1.`data`.dao.FavoritesDao
import np.mad.assignment.mad_assignment_t01_team1.`data`.dao.FavoritesDao_Impl
import np.mad.assignment.mad_assignment_t01_team1.`data`.dao.StallDao
import np.mad.assignment.mad_assignment_t01_team1.`data`.dao.StallDao_Impl
import np.mad.assignment.mad_assignment_t01_team1.`data`.dao.UserDao
import np.mad.assignment.mad_assignment_t01_team1.`data`.dao.UserDao_Impl

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _canteenDao: Lazy<CanteenDao> = lazy {
    CanteenDao_Impl(this)
  }

  private val _stallDao: Lazy<StallDao> = lazy {
    StallDao_Impl(this)
  }

  private val _favoritesDao: Lazy<FavoritesDao> = lazy {
    FavoritesDao_Impl(this)
  }

  private val _userDao: Lazy<UserDao> = lazy {
    UserDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "95a9104084fbcb49e56c153d31969675", "672e423940d644ba808a71e83892c3ca") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `canteens` (`canteenId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `stalls` (`stallId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `canteenId` INTEGER NOT NULL, `name` TEXT NOT NULL, `imageUrl` TEXT, `halal` INTEGER NOT NULL, FOREIGN KEY(`canteenId`) REFERENCES `canteens`(`canteenId`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_stalls_canteenId` ON `stalls` (`canteenId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_stalls_name` ON `stalls` (`name`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `favorites` (`favoriteId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `stallId` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`userId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`stallId`) REFERENCES `stalls`(`stallId`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_favorites_userId_stallId` ON `favorites` (`userId`, `stallId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `users` (`userId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `password` TEXT NOT NULL, `createdDate` TEXT DEFAULT CURRENT_TIMESTAMP)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '95a9104084fbcb49e56c153d31969675')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `canteens`")
        connection.execSQL("DROP TABLE IF EXISTS `stalls`")
        connection.execSQL("DROP TABLE IF EXISTS `favorites`")
        connection.execSQL("DROP TABLE IF EXISTS `users`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsCanteens: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCanteens.put("canteenId", TableInfo.Column("canteenId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCanteens.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCanteens: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCanteens: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCanteens: TableInfo = TableInfo("canteens", _columnsCanteens, _foreignKeysCanteens, _indicesCanteens)
        val _existingCanteens: TableInfo = read(connection, "canteens")
        if (!_infoCanteens.equals(_existingCanteens)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |canteens(np.mad.assignment.mad_assignment_t01_team1.data.entity.CanteenEntity).
              | Expected:
              |""".trimMargin() + _infoCanteens + """
              |
              | Found:
              |""".trimMargin() + _existingCanteens)
        }
        val _columnsStalls: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsStalls.put("stallId", TableInfo.Column("stallId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStalls.put("canteenId", TableInfo.Column("canteenId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStalls.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStalls.put("imageUrl", TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStalls.put("halal", TableInfo.Column("halal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysStalls: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysStalls.add(TableInfo.ForeignKey("canteens", "CASCADE", "NO ACTION", listOf("canteenId"), listOf("canteenId")))
        val _indicesStalls: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesStalls.add(TableInfo.Index("index_stalls_canteenId", false, listOf("canteenId"), listOf("ASC")))
        _indicesStalls.add(TableInfo.Index("index_stalls_name", false, listOf("name"), listOf("ASC")))
        val _infoStalls: TableInfo = TableInfo("stalls", _columnsStalls, _foreignKeysStalls, _indicesStalls)
        val _existingStalls: TableInfo = read(connection, "stalls")
        if (!_infoStalls.equals(_existingStalls)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |stalls(np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity).
              | Expected:
              |""".trimMargin() + _infoStalls + """
              |
              | Found:
              |""".trimMargin() + _existingStalls)
        }
        val _columnsFavorites: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFavorites.put("favoriteId", TableInfo.Column("favoriteId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFavorites.put("userId", TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFavorites.put("stallId", TableInfo.Column("stallId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFavorites: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysFavorites.add(TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", listOf("userId"), listOf("userId")))
        _foreignKeysFavorites.add(TableInfo.ForeignKey("stalls", "CASCADE", "NO ACTION", listOf("stallId"), listOf("stallId")))
        val _indicesFavorites: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFavorites.add(TableInfo.Index("index_favorites_userId_stallId", true, listOf("userId", "stallId"), listOf("ASC", "ASC")))
        val _infoFavorites: TableInfo = TableInfo("favorites", _columnsFavorites, _foreignKeysFavorites, _indicesFavorites)
        val _existingFavorites: TableInfo = read(connection, "favorites")
        if (!_infoFavorites.equals(_existingFavorites)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |favorites(np.mad.assignment.mad_assignment_t01_team1.data.entity.FavoriteEntity).
              | Expected:
              |""".trimMargin() + _infoFavorites + """
              |
              | Found:
              |""".trimMargin() + _existingFavorites)
        }
        val _columnsUsers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUsers.put("userId", TableInfo.Column("userId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("password", TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("createdDate", TableInfo.Column("createdDate", "TEXT", false, 0, "CURRENT_TIMESTAMP", TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUsers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUsers: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUsers: TableInfo = TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers)
        val _existingUsers: TableInfo = read(connection, "users")
        if (!_infoUsers.equals(_existingUsers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |users(np.mad.assignment.mad_assignment_t01_team1.data.entity.UserEntity).
              | Expected:
              |""".trimMargin() + _infoUsers + """
              |
              | Found:
              |""".trimMargin() + _existingUsers)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "canteens", "stalls", "favorites", "users")
  }

  public override fun clearAllTables() {
    super.performClear(true, "canteens", "stalls", "favorites", "users")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(CanteenDao::class, CanteenDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(StallDao::class, StallDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(FavoritesDao::class, FavoritesDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UserDao::class, UserDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun canteenDao(): CanteenDao = _canteenDao.value

  public override fun stallDao(): StallDao = _stallDao.value

  public override fun favoriteDao(): FavoritesDao = _favoritesDao.value

  public override fun userDao(): UserDao = _userDao.value
}
