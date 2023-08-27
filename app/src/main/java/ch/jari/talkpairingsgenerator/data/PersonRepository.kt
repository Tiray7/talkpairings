package ch.jari.talkpairingsgenerator.data

import ch.jari.talkpairingsgenerator.data.database.TalkPairingsDatabase
import ch.jari.talkpairingsgenerator.data.database.models.Person
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonRepository @Inject constructor(
    talkPairingsDatabase: TalkPairingsDatabase,
) {

    private val dao = talkPairingsDatabase.personDao

    suspend fun insertPerson(name: String) = dao.insertPerson(Person(name))

    suspend fun deletePerson(person: Person) = dao.deletePerson(person)

    suspend fun updatePerson(person: Person) = dao.insertPerson(person)

    fun personListFlow() = dao.getPersonsListFlow().map {
        it.sortedBy { person -> person.personName.lowercase() }
    }

    fun getPersonWithFriendsFlow(personName: String) = dao.getPersonWithFriendsFlow(personName)

    fun getPersonsNotFriendsWithFlow(personName: String) =
        dao.getPersonsNotFriendsWithFlow(personName)

    suspend fun insertFriendsRelation(person1: Person, person2: Person) =
        dao.insertFriendsRelation(person1, person2)

    suspend fun removeFriendsRelation(person1: Person, person2: Person) =
        dao.removeFriendsRelation(person1, person2)
}