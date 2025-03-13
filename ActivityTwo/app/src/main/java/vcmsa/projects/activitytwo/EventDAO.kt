package vcmsa.projects.activitytwo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//@Dao
//interface EventDAO {
//
//    @Insert
//    fun insertEvent(eventsEntity: EventsEntity)
//
//    @Query("Select * FROM events")
//    fun getAllEvents():LiveData<List<EventsEntity>>
//}

@Dao
interface EventDAO {
    @Insert
    suspend fun insertEvent(eventsEntity: EventsEntity)

    @Query("SELECT * FROM events")
    fun getAllEvents(): LiveData<List<EventsEntity>>
}