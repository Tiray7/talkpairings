package ch.jari.talkpairingsgenerator.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ch.jari.talkpairingsgenerator.data.database.models.Pairing
import ch.jari.talkpairingsgenerator.data.database.models.PersonWithFriends
import ch.jari.talkpairingsgenerator.data.database.models.addTime
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PairingDao {

    @Query("SELECT MAX(groupId) FROM Pairing")
    protected abstract fun getMaxGroupId(): Long?

    @Insert
    protected abstract suspend fun insertPairing(vararg pairing: Pairing)

    @Delete
    abstract suspend fun deletePairing(vararg pairing: Pairing)

    @Transaction
    @Query("SELECT * FROM Person WHERE selected = 1")
    abstract suspend fun getSelectedPersonsWithFriends(): List<PersonWithFriends>

    @Transaction
    open suspend fun insertPairingsList(pairings: List<Pairing>) {
        if (pairings.isEmpty()) return
        var groupId = pairings.first().groupId
        if (groupId == 0L) {
            groupId = getMaxGroupId()?.plus(1) ?: 1L
        }
        insertPairing(*pairings.addTime().map { it.copy(groupId = groupId) }
            .toTypedArray())
    }

    @Query("SELECT * FROM Pairing")
    abstract fun allPairingsFlow(): Flow<List<Pairing>>

}