package np.mad.assignment.mad_assignment_t01_team1.model

data class CanteenGroupUi(
    val canteenId: Long,
    val canteenName: String,
    val stalls: List<FavoriteStallUi>
)