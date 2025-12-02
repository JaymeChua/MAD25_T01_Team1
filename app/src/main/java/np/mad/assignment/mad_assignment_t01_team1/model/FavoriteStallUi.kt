package np.mad.assignment.mad_assignment_t01_team1.model

data class FavoriteStallUi(
    val favoriteId: Long,
    val stallId: Long,
    val stallName: String,
    val canteenId: Long,
    val canteenName: String,
    val imageUrl: String?,
    val halal:  Boolean = false
)