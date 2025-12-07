package np.mad.assignment.mad_assignment_t01_team1.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity(
    tableName = "reviews",
    indices = [Index(value = ["userId","stallId"], unique = true)], //LLM improvement
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = StallEntity::class,
            parentColumns = ["stallId"],
            childColumns = ["stallId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ],
)
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val reviewId: Long = 0,
    val stallId: Long,
    val userId: Long,
    val username: String,
    val review: String,
    val rating: Int,
    val date: String,
)
