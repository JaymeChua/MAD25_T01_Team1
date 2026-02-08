package np.mad.assignment.mad_assignment_t01_team1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import np.mad.assignment.mad_assignment_t01_team1.data.dao.CanteenDao
import np.mad.assignment.mad_assignment_t01_team1.data.dao.DishDao
import np.mad.assignment.mad_assignment_t01_team1.data.dao.FavoritesDao
import np.mad.assignment.mad_assignment_t01_team1.data.dao.ReviewDao
import np.mad.assignment.mad_assignment_t01_team1.data.dao.StallDao
import np.mad.assignment.mad_assignment_t01_team1.data.dao.UserDao
import np.mad.assignment.mad_assignment_t01_team1.data.entity.CanteenEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.DateConverters
import np.mad.assignment.mad_assignment_t01_team1.data.entity.DishEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.FavoriteEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.UserEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.ReviewEntity

@Database(
    entities = [
        CanteenEntity::class,
        StallEntity::class,
        FavoriteEntity::class,
        UserEntity::class,
        ReviewEntity::class,
        DishEntity::class
    ],
    version = 4,
    exportSchema = true
)
@TypeConverters(DateConverters::class)
abstract  class AppDatabase: RoomDatabase(){
    abstract fun canteenDao(): CanteenDao
    abstract fun stallDao(): StallDao
    abstract fun favoriteDao(): FavoritesDao
    abstract fun userDao(): UserDao

    abstract fun reviewDao(): ReviewDao

    abstract fun dishDao(): DishDao

    companion object{
        @Volatile private  var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mad_team1.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE=it }
            }
    }
}