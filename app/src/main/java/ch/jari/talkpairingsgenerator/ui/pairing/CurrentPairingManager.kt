package ch.jari.talkpairingsgenerator.ui.pairing

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import ch.jari.talkpairingsgenerator.data.PairingRepository
import ch.jari.talkpairingsgenerator.data.database.models.Pairing
import ch.jari.talkpairingsgenerator.data.database.models.addTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentPairingManager @Inject constructor(
    private val pairingRepository: PairingRepository,
) {

    fun groupedPairingsFlow() = pairingRepository.groupedPairingsFlow()

    val currentPairings = mutableStateOf<List<Pairing>>(emptyList())

    val isComputingParing = mutableStateOf(false)

    suspend fun deletePairings(pairings: List<Pairing>) {
        pairingRepository.deletePairings(pairings)
    }

    suspend fun computePairing() {
        if (isComputingParing.value) {
            return
        }

        isComputingParing.value = true
        withContext(Dispatchers.IO) {
            val selectedPersonsWithFriends = pairingRepository.getSelectedPersonsWithFriends().map {
                it.copy(friends = it.friends.filter { friend -> friend.selected })
            }.shuffled()
            val persons = selectedPersonsWithFriends.map { it.person.personName }
            val friendsMap = selectedPersonsWithFriends.associate { personWithFriends ->
                personWithFriends.person.personName to personWithFriends.friends.map { it.personName }
            }

            val pairings = mutableListOf<Pairing>()
            val currentPair = mutableListOf<String>()

            if (findPairings(persons, friendsMap, pairings, currentPair)) {
                pairings.addTime().let {
                    currentPairings.value = it
                    Log.d(TAG, "Pairings found:")
                    pairings.forEach { pairing -> Log.d(TAG, pairing.personNames.toString()) }
                    pairingRepository.insertPairingsList(it)
                }
            } else {
                Log.d(TAG, "No valid pairings found")
            }
        }
        isComputingParing.value = false
    }

    private fun canAddPersonToPair(
        pairing: List<String>,
        newPerson: String,
        friends: Map<String, List<String>>,
    ): Boolean {
        return pairing.none { it in friends[newPerson].orEmpty() }
    }

    private fun findPairings(
        people: List<String>,
        friends: Map<String, List<String>>,
        pairings: MutableList<Pairing>,
        currentPair: MutableList<String>,
    ): Boolean {
        if (currentPair.size == 2) {
            pairings.add(Pairing(personNames = currentPair.toList()))
            currentPair.clear()
        }

        if (people.isEmpty()) {
            if (currentPair.isNotEmpty()) {
                val person = currentPair.first()
                val lastPairing = pairings.last().personNames
                val tripleIsAllowed =
                    canAddPersonToPair(
                        lastPairing,
                        person,
                        friends
                    )
                if (tripleIsAllowed) {
                    val newTriple = lastPairing + person
                    pairings.removeLast()
                    pairings.add(Pairing(personNames = newTriple))
                } else {
                    return false
                }
            }
            return true
        }

        for (person in people) {
            if (canAddPersonToPair(currentPair, person, friends)) {
                currentPair.add(person)
                val remainingPeople = people.filter { it != person }

                if (findPairings(remainingPeople, friends, pairings, currentPair)) return true

                if (currentPair.size == 0) {
                    currentPair.addAll(pairings.removeLast().personNames)
                }
                currentPair.remove(person)
            }
        }

        return false
    }

    companion object {
        private const val TAG = "CurrentPairingManager"
    }
}