package ch.jari.talkpairingsgenerator.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ch.jari.talkpairingsgenerator.data.database.models.Person
import ch.jari.talkpairingsgenerator.data.database.models.PersonCrossRef
import ch.jari.talkpairingsgenerator.data.database.models.PersonWithFriends
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPerson(person: Person)

    @Delete
    abstract suspend fun deletePerson(person: Person)

    @Transaction
    @Query("SELECT * FROM person")
    abstract fun getPersonsListFlow(): Flow<List<Person>>

    @Transaction
    @Query("SELECT * FROM person WHERE personName = :personName")
    abstract fun getPersonWithFriendsFlow(personName: String): Flow<PersonWithFriends>

    @Transaction
    @Query(
        """
        SELECT * FROM Person 
        WHERE personName NOT IN (
            SELECT friendName FROM PersonCrossRef WHERE personName = :personName
        ) AND personName != :personName
    """
    )
    abstract fun getPersonsNotFriendsWithFlow(personName: String): Flow<List<Person>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertPersonCrossRef(personCrossRef: PersonCrossRef)

    open suspend fun insertFriendsRelation(person1: Person, person2: Person) {
        this.insertPersonCrossRef(PersonCrossRef(person1.personName, person2.personName))
        this.insertPersonCrossRef(PersonCrossRef(person2.personName, person1.personName))
    }

    @Delete
    abstract fun deletePersonCrossRef(personCrossRef: PersonCrossRef)

    open suspend fun removeFriendsRelation(person1: Person, person2: Person) {
        this.deletePersonCrossRef(PersonCrossRef(person1.personName, person2.personName))
        this.deletePersonCrossRef(PersonCrossRef(person2.personName, person1.personName))
    }

}

