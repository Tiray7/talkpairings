package ch.jari.talkpairingsgenerator.data

import ch.jari.talkpairingsgenerator.data.database.TalkPairingsDatabase
import ch.jari.talkpairingsgenerator.data.database.models.Pairing
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PairingRepository @Inject constructor(
    talkPairingsDatabase: TalkPairingsDatabase,
) {

    private val dao = talkPairingsDatabase.pairingDao

    suspend fun getSelectedPersonsWithFriends() = dao.getSelectedPersonsWithFriends()

    suspend fun insertPairingsList(pairings: List<Pairing>) = dao.insertPairingsList(pairings)

    fun groupedPairingsFlow() = dao.allPairingsFlow().map {
        it.groupBy { it.groupId }.toList().sortedByDescending { it.first }
    }

    suspend fun deletePairings(pairing: List<Pairing>) = dao.deletePairing(*pairing.toTypedArray())

}