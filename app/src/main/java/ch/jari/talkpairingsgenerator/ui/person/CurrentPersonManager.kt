package ch.jari.talkpairingsgenerator.ui.person

import ch.jari.talkpairingsgenerator.data.database.models.Person
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentPersonManager @Inject constructor() {
    val currentPersonFlow = MutableStateFlow<Person?>(null)

    fun setNewPerson(person: Person) {
        currentPersonFlow.value = person
    }
}