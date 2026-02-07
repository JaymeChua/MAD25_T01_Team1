package np.mad.assignment.mad_assignment_t01_team1.data.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserScopedRefs(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val uidProvider: () -> String
) {
    fun userDoc(): DocumentReference = db.collection("users").document(uidProvider())

    fun favorites(): CollectionReference = userDoc().collection("favorites")

    fun reviews(): CollectionReference = userDoc().collection("reviews")

}