package vcmsa.projects.activitytwo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventsEntity(
@PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val location: String

)