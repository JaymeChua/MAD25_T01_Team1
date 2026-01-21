package np.mad.assignment.mad_assignment_t01_team1.data.firebase.dto

import com.google.firebase.firestore.*
import np.mad.assignment.mad_assignment_t01_team1.data.firebase.FirestorePath

class FirestoreStallService (
    private val db: FirebaseFirestore = FirebaseFirestore.g etInstance()
){
    private fun col() =db.collection(FirestorePath.STALLS)


    fun addStall(
        canteenId: String,
        name: String,
        cuisine: String,
        description: String,
        halal: Boolean,
        imageUrl: String?,
        onSucces: (String)-> Unit,
        onError: (Exception) -> Unit
    ){

    }
}
