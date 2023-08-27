package ch.jari.talkpairingsgenerator.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ch.jari.talkpairingsgenerator.data.database.dao.PairingDao
import ch.jari.talkpairingsgenerator.data.database.dao.PersonDao
import ch.jari.talkpairingsgenerator.data.database.models.Pairing
import ch.jari.talkpairingsgenerator.data.database.models.Person
import ch.jari.talkpairingsgenerator.data.database.models.PersonCrossRef


@Database(
    entities = [
        Person::class,
        PersonCrossRef::class,
        Pairing::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TalkPairingsDatabase : RoomDatabase() {

    abstract val personDao: PersonDao

    abstract val pairingDao: PairingDao

    companion object {
        @Volatile
        private var INSTANCE: TalkPairingsDatabase? = null

        fun getInstance(context: Context): TalkPairingsDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TalkPairingsDatabase::class.java,
                    "talk_pairings_db"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}