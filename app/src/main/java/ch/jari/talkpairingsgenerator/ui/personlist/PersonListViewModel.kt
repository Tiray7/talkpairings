package ch.jari.talkpairingsgenerator.ui.personlist

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.jari.talkpairingsgenerator.data.PersonRepository
import ch.jari.talkpairingsgenerator.data.database.models.Person
import ch.jari.talkpairingsgenerator.ui.pairing.CurrentPairingManager
import ch.jari.talkpairingsgenerator.ui.person.CurrentPersonManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonListViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    val currentPersonManager: CurrentPersonManager,
    val currentPairingManager: CurrentPairingManager,
) : ViewModel() {

    fun generatePairing() {
        viewModelScope.launch {
            currentPairingManager.computePairing()
        }
    }

    val listOfPersons = personRepository.personListFlow()

    val addPersonState = mutableStateOf(TextFieldValue())

    fun addPerson() {
        addPersonState.value.text.let {
            if (it.isNotEmpty()) {
                viewModelScope.launch {
                    personRepository.insertPerson(it)
                    addPersonState.value = TextFieldValue()
                }
            }
        }
    }

    fun setCurrentPerson(person: Person) {
        currentPersonManager.setNewPerson(person)
    }

    fun deletePerson(person: Person) {
        viewModelScope.launch {
            personRepository.deletePerson(person)
        }
    }

    fun updatePerson(person: Person) {
        viewModelScope.launch {
            personRepository.updatePerson(person)
        }
    }
}