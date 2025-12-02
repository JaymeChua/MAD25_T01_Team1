package np.mad.assignment.mad_assignment_t01_team1.data.db

import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import np.mad.assignment.mad_assignment_t01_team1.data.entity.*

suspend fun seedMockData(db: AppDatabase) = withContext(Dispatchers.IO){
    db.withTransaction {
        val userId = db.userDao().upsert(
            UserEntity(userId = 1L, name = "demo", password = "pass_demo", createdDate = null)
        )

        val canteenIds = db.canteenDao().insert(
            CanteenEntity(name = "Food Club"),
            CanteenEntity(name = "Makan Place"),
            CanteenEntity(name = "Munch")
        )
        val foodClubId = canteenIds.getOrNull(0) ?: error("Canteen 'Food Club' not found after insert")
        val makanPlaceId = canteenIds.getOrNull(1) ?: error("Canteen 'Makan Place' not found after insert")
        val munchId = canteenIds.getOrNull(2) ?: error("Canteen 'Munch' not found after insert")

        db.stallDao().insert(
            StallEntity(name = "Chicken Rice", canteenId = foodClubId, halal = true),
            StallEntity(name = "Ban Mian", canteenId = foodClubId, halal = true),
            StallEntity(name = "Mala Hotpot", canteenId = makanPlaceId, halal = false),
            StallEntity(name = "Western", canteenId = munchId, halal = false),
        )
        val chickenRiceStallId = db.stallDao().getByName("Chicken Rice")?.stallId ?: error("Stall 'Chicken Rice' not found after insert")

        db.favoriteDao().addFavorite(
            FavoriteEntity(userId = userId, stallId = chickenRiceStallId)
        )
    }
}