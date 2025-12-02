package np.mad.assignment.mad_assignment_t01_team1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("canteens")
data class CanteenEntity(
    @PrimaryKey(autoGenerate = true)val canteenId: Long = 0,
    val name: String
)