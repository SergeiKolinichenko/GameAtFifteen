package info.sergeikolinichenko.gameatfifteen.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import info.sergeikolinichenko.gameatfifteen.models.ScoreGameDbModel

/** Created by Sergei Kolinichenko on 26.01.2023 at 15:50 (GMT+3) **/

@Database(
    entities = [ScoreGameDbModel::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "game_database.db"

        // 1.7: board sizes. Every result saved before was a 4x4 game
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE scores ADD COLUMN boardSize INTEGER NOT NULL DEFAULT 4")
            }
        }

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}