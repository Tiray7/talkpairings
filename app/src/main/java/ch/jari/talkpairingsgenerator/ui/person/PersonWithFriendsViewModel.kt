package ch.jari.talkpairingsgenerator.ui.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.jari.talkpairingsgenerator.data.PersonRepository
import ch.jari.talkpairingsgenerator.data.database.models.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class PersonWithFriendsViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val currentPersonManager: CurrentPersonManager,
) : ViewModel() {

    val personWithFriendsFlow = currentPersonManager.currentPersonFlow.flatMapLatest { personName ->
        personName?.let { personRepository.getPersonWithFriendsFlow(it.personName) }
            ?: emptyFlow()
    }

    val personNotFriendsWithFlow =
        currentPersonManager.currentPersonFlow.flatMapLatest { personName ->
            personName?.let { personRepository.getPersonsNotFriendsWithFlow(it.personName) }
                ?: emptyFlow()
        }

    fun addFriend(friend: Person) {
        viewModelScope.launch((Dispatchers.IO)) {
            currentPersonManager.currentPersonFlow.value?.let {
                personRepository.insertFriendsRelation(it, friend)
            }
        }
    }

    fun removeFriend(friend: Person) {
        viewModelScope.launch(Dispatchers.IO) {
            currentPersonManager.currentPersonFlow.value?.let {
                personRepository.removeFriendsRelation(it, friend)
            }
        }
    }

}