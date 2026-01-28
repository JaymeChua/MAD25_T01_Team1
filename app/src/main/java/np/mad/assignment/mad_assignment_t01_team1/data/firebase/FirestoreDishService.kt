package np.mad.assignment.mad_assignment_t01_team1.data.firebase

import android.R
import androidx.compose.animation.core.snap
import com.google.firebase.firestore.*
import np.mad.assignment.mad_assignment_t01_team1.data.firebase.dto.DishRemote
import com.google.firebase.firestore.Query

class FirestoreDishService(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private fun col() =db.collection(FirestorePath.DISHES)


    fun addDish(
         stallId: Int,
         dishName: String,
         dishPrice: String,
         imageUrl: String?,
         onSuccess: (String) -> Unit,
         onError: (Exception) -> Unit
    ){
        val data = hashMapOf(
            "stallId" to stallId,
            "dishName" to dishName,
            "dishPrice" to dishPrice,
            "imageUrl" to imageUrl,
            "updatedAt" to System.currentTimeMillis()
        )
        col().add(data).addOnSuccessListener { onSuccess(it.id) }.addOnFailureListener(onError)
    }


    fun getDish(
        dishId: String,
        onSuccess: (DishRemote?) -> Unit,
        onError: (Exception) -> Unit
    ){
        col().document().get().addOnSuccessListener { snap -> onSuccess(snap.toObject(DishRemote::class.java))}.addOnFailureListener(onError)
    }


    fun queryDisheshByStall(
        stallId: Int,
        onSuccess: (List<Pair<String, DishRemote>>) -> Unit,
        onError: (Exception) -> Unit
    ){
        col().whereEqualTo("stallId",stallId).orderBy("dishName", Query.Direction.ASCENDING).get().addOnSuccessListener { q -> val items = q.documents.mapNotNull { d->d.toObject(
            DishRemote::class.java)?.let { d.id to it } }
            onSuccess(items)
        }.addOnFailureListener(onError)
    }


    fun updateDish(
        dishId: String,
        updates: Map<String, Any?>,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        col().document(dishId).update(updates).addOnSuccessListener { onSuccess() }.addOnFailureListener(onError)
    }


    fun deleteDish(
        dishId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        col().document(dishId).delete().addOnSuccessListener { onSuccess() }.addOnFailureListener(onError)
    }
}
