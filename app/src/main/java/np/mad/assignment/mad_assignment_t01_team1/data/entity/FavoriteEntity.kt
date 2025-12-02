package np.mad.assignment.mad_assignment_t01_team1.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorites",
    indices = [Index(value = ["userId","stallId"], unique = true)],
    foreignKeys = [
        ForeignKey(
             UserEntity::class,
            ["userId"],
            ["userId"],
            ForeignKey.CASCADE
        ),
        ForeignKey(
            StallEntity::class,
            ["stallId"],
            ["stallId"],
            ForeignKey.CASCADE
        )
    ]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val favoriteId: Long = 0,
    val userId: Long,
    val stallId: Long
)