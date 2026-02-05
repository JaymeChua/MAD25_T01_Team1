package np.mad.assignment.mad_assignment_t01_team1.data.firebase

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import np.mad.assignment.mad_assignment_t01_team1.data.firebase.dto.FavoriteRemote

class FirestoreFavoriteService (
    private val refs: UserScopedRefs,
    private val now: ()-> Long = { System.currentTimeMillis() }
){
    private fun col() = refs.favorites(),

    fun putFavorite(
        stallId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        val data = FavoriteRemote(
            stallId = stallId,
            createdAt = now()
        )
        col().document(stallId).set(data, SetOptions.merge()).addOnSuccessListener { onSuccess() }.addOnFailureListener(onError)
    }

    fun deleteFavoriteByStall(
        stallId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        col().document(stallId).delete().addOnSuccessListener { onSuccess() }.addOnFailureListener(onError)
    }

    fun isFavorite(
        stallId: String,
        onSuccess: (Boolean) -> Unit,
        onError: (Exception) -> Unit
    ) {
        col().document(stallId).get().addOnSuccessListener { snap -> onSuccess(snap.exists()) }
            .addOnFailureListener(onError)
    }

    fun listFavorites(
        onSuccess: (List<Pair<String, FavoriteRemote>>) -> Unit,
        onError: (Exception) -> Unit
    ){
        col().orderBy("createdAt", Query.Direction.DESCENDING).get().addOnSuccessListener { q ->
            val items = q.documents.mapNotNull { d ->
                d.toObject(
                    FavoriteRemote::class.java
                )?.let { d.id to it }
            }
            onSuccess(items)
        }.addOnFailureListener(onError)
    }


}