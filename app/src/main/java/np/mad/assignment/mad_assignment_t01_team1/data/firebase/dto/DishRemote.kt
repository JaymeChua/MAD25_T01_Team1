package np.mad.assignment.mad_assignment_t01_team1.data.firebase.dto

data class DishRemote(
    val stallId: String = "",
    val dishName: String = "",
    val dishPrice: String = "",
    val imageUrl: String? = null,
    val updatedAt: Long = 0L
)
