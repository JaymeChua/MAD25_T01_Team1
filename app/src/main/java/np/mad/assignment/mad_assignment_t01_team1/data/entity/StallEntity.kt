package np.mad.assignment.mad_assignment_t01_team1.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stalls",
    [Index("canteenId"), Index("name")],
    foreignKeys = [
        ForeignKey(
            entity = CanteenEntity::class,
            parentColumns = ["canteenId"],
            childColumns = ["canteenId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StallEntity(
    @PrimaryKey(autoGenerate = true) val stallId: Long = 0,
    val canteenName: String,
    val canteenId: Long,
    val cuisine: String,
    val description: String,
    val name: String,
    val imageResId: Int,
    val halal: Boolean = false
)