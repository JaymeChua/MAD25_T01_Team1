package np.mad.assignment.mad_assignment_t01_team1.data.firebase

import androidx.compose.animation.core.snap
import com.google.firebase.firestore.FirebaseFirestore
import np.mad.assignment.mad_assignment_t01_team1.data.firebase.dto.UserProfileRemote
import kotlin.jvm.java

class FirestoreUserService(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val refs: UserScopedRefs
) {
    fun putUserProfile(
        profile: UserProfileRemote,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        refs.userDoc().set(profile).addOnSuccessListener { onSuccess() }.addOnFailureListener(onError)
    }

    fun updateUserProfile(
        updates: Map<String, Any?>,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        refs.userDoc().update(updates).addOnSuccessListener { onSuccess() }.addOnFailureListener(onError)
    }

    fun getUserProfile(
        onSuccess: (UserProfileRemote?) -> Unit,
        onError: (Exception) -> Unit
    ){
        refs.userDoc().get().addOnSuccessListener { snap -> onSuccess(snap.toObject(
            UserProfileRemote::class.java)) }.addOnFailureListener(onError)
    }
}