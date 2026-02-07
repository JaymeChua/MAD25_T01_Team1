package np.mad.assignment.mad_assignment_t01_team1.data.firebase.dto

data class StallRemote(
    val canteenId: String = "",
    val name: String = "",
    val cuisine: String = "",
    val description: String = "",
    val halal: Boolean = false,
    val imageUrl: String? = null,
    val updatedAt: Long = 0L
)
