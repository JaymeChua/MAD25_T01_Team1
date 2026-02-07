package np.mad.assignment.mad_assignment_t01_team1.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dishes",
    indices = [Index(value = ["stallId"])],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = StallEntity::class,
            parentColumns = ["stallId"],
            childColumns = ["stallId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ],
)

data class DishEntity (
    @PrimaryKey(autoGenerate = true) val dishId: Long = 0,
    val stallId: Long,
    val dishName: String,
    val dishPrice: String,
    val imageResId: Int? = null,
    val imagePath: String? = null
)