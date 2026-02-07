package np.mad.assignment.mad_assignment_t01_team1.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.entity.CanteenEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.DishEntity

class PeriodicSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val firestore = FirebaseFirestore.getInstance()


    fun startPeriodicSyncWorker(context: Context) {
        val request = PeriodicWorkRequestBuilder<PeriodicSyncWorker>(
            15, java.util.concurrent.TimeUnit.SECONDS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "FirestoreRoomSync",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
    override suspend fun doWork(): Result {
        val db = AppDatabase.get(applicationContext)

        // ---- STEP 1: ROOM → GET LOCAL SNAPSHOTS ----
        val localCanteens = db.canteenDao().getAllNow()
        val localStalls = db.stallDao().getAllStallsNow()
        val localDishes = db.dishDao().getAllDishesNow()

        // ---- STEP 2: FIRESTORE → READ CLOUD SNAPSHOTS ----
        val remoteCanteens = firestore.collection("canteens").get().await()
        val remoteStalls = firestore.collection("stalls").get().await()
        val remoteDishes = firestore.collection("dishes").get().await()

        // Put into maps for diff
        val remoteCanteenMap = remoteCanteens.documents.associateBy { it.id }
        val remoteStallMap = remoteStalls.documents.associateBy { it.id }
        val remoteDishMap = remoteDishes.documents.associateBy { it.id }

        // ---- STEP 3: SYNC CANTEENS ----
        syncCanteens(db, localCanteens, remoteCanteenMap)

        // ---- STEP 4: SYNC STALLS ----
        syncStalls(db, localStalls, remoteStallMap)

        // ---- STEP 5: SYNC DISHES ----
        syncDishes(db, localDishes, remoteDishMap)

        return Result.success()
    }

    // ----------------- SYNC LOGIC -------------------

    private suspend fun syncCanteens(
        db: AppDatabase,
        localList: List<CanteenEntity>,
        remoteMap: Map<String, com.google.firebase.firestore.DocumentSnapshot>
    ) {
        val canteenDao = db.canteenDao()

        // LOCAL → REMOTE
        for (canteen in localList) {
            val id = canteen.canteenId.toString()
            if (!remoteMap.containsKey(id)) {
                firestore.collection("canteens").document(id).set(
                    mapOf("name" to canteen.name)
                ).await()
            }
        }

        // REMOTE → LOCAL
        for ((id, snap) in remoteMap) {
            val name = snap.getString("name") ?: continue
            val localExists = localList.any { it.canteenId.toString() == id }

            if (!localExists) {
                canteenDao.insert(
                    CanteenEntity(
                        canteenId = id.toLong(),
                        name = name
                    )
                )
            }
        }
    }

    private suspend fun syncStalls(
        db: AppDatabase,
        localList: List<StallEntity>,
        remoteMap: Map<String, com.google.firebase.firestore.DocumentSnapshot>
    ) {
        val stallDao = db.stallDao()

        // LOCAL → REMOTE
        for (stall in localList) {
            val id = stall.stallId.toString()
            if (!remoteMap.containsKey(id)) {
                firestore.collection("stalls").document(id).set(
                    mapOf(
                        "stallId" to stall.stallId,
                        "canteenName" to stall.canteenName,
                        "canteenId" to stall.canteenId,
                        "cuisine" to stall.cuisine,
                        "description" to stall.description,
                        "name" to stall.name,
                        "imageResId" to stall.imageResId,
                        "halal" to stall.halal
                    )
                ).await()
            }
        }

        // REMOTE → LOCAL
        for ((id, snap) in remoteMap) {
            val stallId = id.toLong()

            if (localList.none { it.stallId == stallId }) {
                val entity = StallEntity(
                    stallId = stallId,
                    canteenName = snap.getString("canteenName") ?: "",
                    canteenId = snap.getLong("canteenId") ?: 0L,
                    cuisine = snap.getString("cuisine") ?: "",
                    description = snap.getString("description") ?: "",
                    name = snap.getString("name") ?: "",
                    imageResId = (snap.getLong("imageResId") ?: 0L).toInt(),
                    halal = snap.getBoolean("halal") ?: false
                )
                stallDao.insert(entity)
            }
        }
    }

    private suspend fun syncDishes(
        db: AppDatabase,
        localList: List<DishEntity>,
        remoteMap: Map<String, com.google.firebase.firestore.DocumentSnapshot>
    ) {
        val dishDao = db.dishDao()

        // LOCAL → REMOTE
        for (dish in localList) {
            val id = dish.dishId.toString()
            if (!remoteMap.containsKey(id)) {
                firestore.collection("dishes").document(id).set(
                    mapOf(
                        "dishId" to dish.dishId,
                        "dishName" to dish.dishName,
                        "dishPrice" to dish.dishPrice,
                        "imageResId" to dish.imageResId,
                        "stallId" to dish.stallId
                    )
                ).await()
            }
        }

        // REMOTE → LOCAL
        for ((id, snap) in remoteMap) {
            val dishId = id.toLong()

            if (localList.none { it.dishId == dishId }) {
                val entity = DishEntity(
                    dishId = dishId,
                    stallId = snap.getLong("stallId") ?: 0L,
                    dishName = snap.getString("dishName") ?: "",
                    dishPrice = snap.getString("dishPrice") ?: "",
                    imageResId = (snap.getLong("imageResId") ?: 0L).toInt()
                )
                dishDao.insert(entity)
            }
        }
    }
}