package np.mad.assignment.mad_assignment_t01_team1.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import np.mad.assignment.mad_assignment_t01_team1.data.firebase.dto.StallRemote

class FirestoreStallService (
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
){
    private fun col() =db.collection(FirestorePath.STALLS)


    fun addStall(
        canteenId: String,
        name: String,
        cuisine: String,
        description: String,
        halal: Boolean,
        imageUrl: String?,
        onSuccess: (String)-> Unit,
        onError: (Exception) -> Unit
    ){
        val data = hashMapOf(
            "canteenId" to canteenId,
            "name" to name,
            "cuisine" to cuisine,
            "description" to description,
            "halal" to halal,
            "imageUrl" to imageUrl,
            "updatedAt" to System.currentTimeMillis()
        )
        col().add(data).addOnSuccessListener { onSuccess(it.id) }.addOnFailureListener(onError)
    }

    fun getStall(
        stallId: String,
        onSuccess: (StallRemote?) -> Unit,
        onError: (Exception) -> Unit
    ){
        col().document(stallId).get().addOnSuccessListener { snap -> onSuccess(snap.toObject(
            StallRemote::class.java)) }.addOnFailureListener(onError)
    }

    fun queryStallsByCanteen(
        canteenId: String,
        onSuccess: (List<Pair<String, StallRemote>>) -> Unit,
        onError: (Exception) -> Unit
    ){
        col().whereEqualTo("canteenId",canteenId).orderBy("name", Query.Direction.ASCENDING).get().addOnSuccessListener { q-> val items = q.documents.mapNotNull { d->d.toObject(
            StallRemote::class.java)?.let { d.id to it }
        }
            onSuccess(items)
        }.addOnFailureListener(onError)
    }

    fun updateStall(
        stallId: String,
        updates: Map<String, Any?>,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        col().document(stallId).update(updates).addOnFailureListener { onSuccess() }.addOnFailureListener(onError)
    }

    fun deleteStall(
        stallId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        col().document(stallId).delete().addOnSuccessListener { onSuccess() }.addOnFailureListener(onError)
    }
}