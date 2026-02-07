package np.mad.assignment.mad_assignment_t01_team1.data.firebase

import androidx.compose.animation.core.snap
import np.mad.assignment.mad_assignment_t01_team1.data.firebase.dto.CanteenRemote
import com.google.firebase.firestore.*
class FirestoreCanteenService (

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private fun col() = db.collection(FirestorePath.CANTEENS)

    fun addCanteen(
        name: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val now = System.currentTimeMillis()
        val data = hashMapOf(
            "name" to name,
            "createdAt" to now
        )
        col().add(data).addOnSuccessListener { onSuccess(it.id) }.addOnFailureListener(onError)
    }

    fun putCanteenWithId(
        canteenId: String,
        payload: CanteenRemote,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        col().document(canteenId).set(payload).addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onError)
    }

    fun getCanteen(
        canteenId: String,
        onSuccess: (CanteenRemote?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        col().document(canteenId).get().addOnSuccessListener { snap ->
            onSuccess(
                snap.toObject(
                    CanteenRemote::class.java
                )
            )
        }.addOnFailureListener(onError)
    }

    fun listCanteens(
        onSuccess: (List<Pair<String, CanteenRemote>>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        col().orderBy("name", Query.Direction.ASCENDING).get().addOnSuccessListener { q ->
            val items = q.documents.mapNotNull { d ->
                d.toObject(
                    CanteenRemote::class.java
                )?.let { d.id to it }
            }
            onSuccess(items)
        }.addOnFailureListener(onError)
    }


    fun updateCanteen(
        canteenId: String,
        updates: Map<String, Any?>,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        col().document(canteenId).update(updates).addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onError)

    }

    fun deleteCanteen(
        canteenId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        col().document(canteenId).delete().addOnSuccessListener { onSuccess() }.addOnFailureListener(onError)
    }


}
