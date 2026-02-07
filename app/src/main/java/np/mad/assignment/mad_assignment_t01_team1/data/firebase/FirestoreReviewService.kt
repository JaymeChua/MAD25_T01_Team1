package np.mad.assignment.mad_assignment_t01_team1.data.firebase

import androidx.compose.animation.core.snap
import androidx.room.util.query
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import np.mad.assignment.mad_assignment_t01_team1.data.firebase.dto.ReviewRemote

class FirestoreReviewService(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private fun col() = db.collection("reviews")

    fun addReview(
        stallId: String,
        username: String,
        review: String,
        rating: Int,
        date: String,
        onSuccess: (String)-> Unit,
        onError: (Exception) -> Unit
    ){
        val data = ReviewRemote(
            stallId = stallId,
            username = username,
            review = review,
            rating = rating,
            date = date,
            updatedAt = System.currentTimeMillis()
        )
        col().add(data).addOnSuccessListener { onSuccess(it.id) }.addOnFailureListener(onError)
    }

    fun getReview(
        reviewId: String,
        onSuccess: (ReviewRemote?) -> Unit,
        onError: (Exception) -> Unit
    ){
        col().document(reviewId).get().addOnSuccessListener { snap -> onSuccess(snap.toObject(
            ReviewRemote::class.java)) }.addOnFailureListener(onError)
    }

    fun listReviewsForStall(
        stallId: String,
        onSuccess: (List<Pair<String,ReviewRemote>>) -> Unit,
        onError: (Exception) -> Unit
    ){
        col().whereEqualTo("stallId",stallId).orderBy("updateAt", Query.Direction.DESCENDING).get().addOnSuccessListener { q-> val items = q.documents.mapNotNull { d -> d.toObject(
            ReviewRemote::class.java)?.let { d.id to it }
        }
        onSuccess(items)
        }
            .addOnFailureListener(onError)
    }
    fun updateReview(
        reviewId: String,
        updates: Map<String, Any?>,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        val payload=  updates.toMutableMap().apply {
            this["updatedAt"] = System.currentTimeMillis()
        }
        col().document(reviewId).update(payload).addOnSuccessListener { onSuccess() }.addOnFailureListener(onError)
    }

    fun deleteReview(
        reviewId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        col().document(reviewId).delete().addOnFailureListener { onSuccess() }.addOnFailureListener(onError)
    }

}