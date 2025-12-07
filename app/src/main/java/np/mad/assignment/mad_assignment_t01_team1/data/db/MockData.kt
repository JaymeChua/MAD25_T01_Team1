package np.mad.assignment.mad_assignment_t01_team1.data.db

import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import np.mad.assignment.mad_assignment_t01_team1.FavoriteScreen
import np.mad.assignment.mad_assignment_t01_team1.R
import np.mad.assignment.mad_assignment_t01_team1.data.entity.*
import java.time.LocalDate

suspend fun seedMockData(db: AppDatabase) = withContext(Dispatchers.IO){
    db.clearAllTables()// LLM
    try {
        db.openHelper.writableDatabase.execSQL("DELETE FROM sqlite_sequence")
    } catch (e: Exception) {
        e.printStackTrace()
    }//LLM
    db.withTransaction {
        val userId = db.userDao().upsert(
            UserEntity(userId = 1L, name = "demo", password = "pass_demo", createdDate = null)
        )
        val userId1 = db.userDao().upsert(
            UserEntity(userId = 2L, name = "demo1", password = "pass_demo", createdDate = null)
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
            StallEntity(name = "Chicken Rice",canteenName = "Food Club", canteenId = foodClubId, cuisine = "Asian",imageResId = R.drawable.chicken_rice, description = "A cozy chicken rice food stall offering tender chicken with fragrant rice.", halal = true),
            StallEntity(name = "Mala Hotpot", canteenName = "Makan Place",canteenId = makanPlaceId, cuisine = "Asian",imageResId = R.drawable.mala_xiangguo, description = "Serving delicious mala xiang guo with a variety of ingredients.",halal = false),
            StallEntity(name = "Indonesian", canteenName = "Food Club", canteenId = foodClubId, cuisine = "Indonesian", imageResId = R.drawable.ayampenyat,description = "Serving the best indonesian food including ayam penyat and mee reebus.", halal = true),
            StallEntity(name = "Western", canteenName = "Munch", canteenId = munchId, cuisine = "Western", imageResId = R.drawable.western_food,description = "Serving the best western dishes, including pastas, pizzas, and more.", halal = false),
            StallEntity(name = "Noodle Delights", canteenName = "Makan Place", canteenId = makanPlaceId, cuisine = "Asian", imageResId = R.drawable.bakchormee,description = "Serving delicious noodle dishes, including Singapore Laksa, Mini Pot Noodle, and Famous Lor Mee, all at affordable prices.", halal = false),
            StallEntity(name = "Korean", canteenName = "Munch", canteenId = munchId, cuisine = "Korean", imageResId = R.drawable.bbqcrispychicken,description = "Serving delicious korean food from bibimap to kimchi fried rice.", halal = false),
            StallEntity(name = "Ban Mian", canteenName = "Makan Place", canteenId = makanPlaceId, cuisine = "Asian", imageResId = R.drawable.banmian,description = "Serving delicious noodle dishes, including ban mian and U mian, all at affordable prices.", halal = false),
            )
        val chickenRiceStallId = db.stallDao().getByName("Chicken Rice")?.stallId ?: error("Stall 'Chicken Rice' not found after insert")
        val indonesianStallId = db.stallDao().getByName("Indonesian")?.stallId ?: error("Stall 'Indonesian' not found after insert")
        val noodleStallId = db.stallDao().getByName("Noodle Delights")?.stallId ?: error("Stall 'Noodle delights' not found after insert")
        val banMianStallId = db.stallDao().getByName("Ban Mian")?.stallId ?: error("Stall 'Ban Mian' not found after insert")
        val malaHotpotStallId = db.stallDao().getByName("Mala Hotpot")?.stallId ?: error("Stall 'Mala' not found after insert")
        val westernStallId = db.stallDao().getByName("Western")?.stallId ?: error("Stall 'Western' not found after insert")
        val koreanStallId = db.stallDao().getByName("Korean")?.stallId ?: error("Stall 'Korean' not found after insert")
        val name1 = db.userDao().getById(userId)?.name ?: "Unknown"
        val name2 = db.userDao().getById(userId1)?.name ?: "Unknown"
        db.reviewDao().addReviews(
            //ReviewEntity(userId = userId, username = name1, review = "Yoo this food is bussin. Unc locked in", rating = 5, stallId = chickenRiceStallId, date = LocalDate.now()),
            ReviewEntity(userId = userId1, username = name2, review = "It was half-uncooked bro. This uncle trolling", rating = 1, stallId = chickenRiceStallId, date = "2025-02-16"),
        )
        db.dishDao().insert(
            DishEntity(
                stallId = indonesianStallId,
                dishName = "Ayam Penyet Set",
                dishPrice = "4.00",
                imageResId = R.drawable.ayampenyat
            ),
            DishEntity(
                stallId = indonesianStallId,
                dishName = "Ikan Bawal Set",
                dishPrice = "3.80",
                imageResId = R.drawable.ikanbawal
            ),
            DishEntity(
                stallId = indonesianStallId,
                dishName = "Udang Penyet Set",
                dishPrice = "3.90",
                imageResId = R.drawable.udangpenyat
            ),
            DishEntity(
                stallId = indonesianStallId,
                dishName = "Ikan Dory Set",
                dishPrice = "4.00",
                imageResId = R.drawable.ikandori
            ),
            DishEntity(
                stallId = indonesianStallId,
                dishName = "Curry Chicken Set",
                dishPrice = "4.00",
                imageResId = R.drawable.currychicken
            ),
            DishEntity(
                stallId = indonesianStallId,
                dishName = "Mee Rebus",
                dishPrice = "3.00",
                imageResId = R.drawable.meerebus
            ),
            DishEntity(
                stallId = indonesianStallId,
                dishName = "Mee Siam",
                dishPrice = "2.50",
                imageResId = R.drawable.meesiam
            ),
            //Ethan's dishes (Makan place, Noodle store)
            DishEntity(
                stallId = noodleStallId,
                dishName = "Laksa",
                dishPrice = "4.50",
                imageResId = R.drawable.laksa
            ),
            DishEntity(
                stallId = noodleStallId,
                dishName = "Prawn Noodle",
                dishPrice = "5.00",
                imageResId = R.drawable.prawnnoodle
            ),
            DishEntity(
                stallId = noodleStallId,
                dishName = "Mushroom Minced Meat Noodle",
                dishPrice = "4.50",
                imageResId = R.drawable.mushroommincedmeatnoodle
            ),
            DishEntity(
                stallId = noodleStallId,
                dishName = "Lor Mee",
                dishPrice = "4.50",
                imageResId = R.drawable.lormee
            ),
            DishEntity(
                stallId = noodleStallId,
                dishName = "Chicken Meatball Noodle",
                dishPrice = "4.30",
                imageResId = R.drawable.chickenmeatballnoodle
            ),
            DishEntity(
                stallId = noodleStallId,
                dishName = "Chicken Cutlet Noodle",
                dishPrice = "5.00",
                imageResId = R.drawable.chickencutletnoodle
            ),
            DishEntity(
                stallId = noodleStallId,
                dishName = "Mini Pot Noodle",
                dishPrice = "5.00",
                imageResId = R.drawable.minipot
            )
        )
        db.dishDao().insert(
            DishEntity(stallId = koreanStallId, dishName = "BBQ Crispy Chicken Omelette Set", dishPrice = "4.80", imageResId = R.drawable.bbqcrispychicken),
                DishEntity(stallId = koreanStallId, dishName = "Hot Stone Bibimap", dishPrice = "5.30", imageResId = R.drawable.hotstonebibimap),
                DishEntity(stallId = koreanStallId, dishName = "Chicken Cutlet Mayo and Omelette Curry Rice", dishPrice = "4.50", imageResId = R.drawable.chickencutlet),
            DishEntity(stallId = koreanStallId, dishName = "Fried Prawn Mayo and Omelette Curry Rice", dishPrice = "4.80", imageResId = R.drawable.friedprawn),
            DishEntity(stallId = koreanStallId, dishName = "Fish Fillet Mayo and Omelette Curry Rice", dishPrice = "4.50", imageResId = R.drawable.fishfillet),
            DishEntity(stallId = koreanStallId, dishName = "Sundubu(Spicy Tofu Stew)", dishPrice = "4.00", imageResId = R.drawable.sundubu),
            DishEntity(stallId = koreanStallId, dishName = "Bulgoga Jungol(Beef Stew)", dishPrice = "5.00", imageResId = R.drawable.bulgoga),
            DishEntity(stallId = koreanStallId, dishName = "Kimchi Jjigae(Spicy Kimchi Stew)", dishPrice = "4.00", imageResId = R.drawable.spicykimchistew),)
        db.dishDao().insert(
            DishEntity(stallId = banMianStallId, dishName = "Minced Meat Paste Noodles", dishPrice = "7.50", imageResId = R.drawable.mincedmeat),
            DishEntity(stallId = banMianStallId, dishName = "Fried Fish Bee Hoon", dishPrice = "7.50", imageResId = R.drawable.friedfishbeehoon),
            DishEntity(stallId = banMianStallId, dishName = "Signature La Mian", dishPrice = "7.50", imageResId = R.drawable.lamian),
            DishEntity(stallId = banMianStallId, dishName = "Dumpling La Mian", dishPrice = "7.50", imageResId = R.drawable.dumplinglamian),
            DishEntity(stallId = banMianStallId, dishName = "Ban Mian", dishPrice = "6.50", imageResId = R.drawable.banmian),
            DishEntity(stallId = banMianStallId, dishName = "You Mian", dishPrice = "6.50", imageResId = R.drawable.umian),
        )
            db.favoriteDao().addFavorites(
            FavoriteEntity(userId = userId, stallId = chickenRiceStallId),
            FavoriteEntity(userId = userId, stallId = banMianStallId),
            FavoriteEntity(userId=userId, stallId = malaHotpotStallId),
            FavoriteEntity(userId=userId, stallId = westernStallId),
            FavoriteEntity(userId=userId, stallId = noodleStallId)
        )
    }
}