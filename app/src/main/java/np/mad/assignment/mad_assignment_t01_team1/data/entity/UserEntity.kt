package np.mad.assignment.mad_assignment_t01_team1.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val name: String,
    val password: String,
    val role: String = "USER",
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val createdDate: String? = null
)