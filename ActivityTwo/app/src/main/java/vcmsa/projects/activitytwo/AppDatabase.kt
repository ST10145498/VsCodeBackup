package vcmsa.projects.activitytwo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(entities = [EventsEntity::class], version = 1, exportSchema = false)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun eventDao(): EventDAO
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "activitytwo_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}

@Database(
    entities = [EventsEntity::class, UserEntity::class],  // Add UserEntity here
    version = 2,  // Increment version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDAO
    abstract fun userDao(): UserDAO  // Add user DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "activitytwo_database"
                )
                    .fallbackToDestructiveMigration()  // Add this for version change
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}