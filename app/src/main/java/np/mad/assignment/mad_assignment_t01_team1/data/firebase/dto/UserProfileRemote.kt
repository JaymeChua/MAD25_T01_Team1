package np.mad.assignment.mad_assignment_t01_team1.data.firebase.dto

data class UserProfileRemote(
    val displayName: String = "",
    val role: String = "USER", //"USER" or "ADMIN"
    val createdAt: Long =0L,
    val email: String? = null,
    val firebaseUid: String? = null
)
