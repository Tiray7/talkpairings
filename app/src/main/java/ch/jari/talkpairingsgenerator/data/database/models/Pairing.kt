package ch.jari.talkpairingsgenerator.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity
data class Pairing(
    @PrimaryKey(autoGenerate = true)
    val pairingId: Long = 0L,
    val groupId: Long = 0L,
    val personNames: List<String>,
    val currentTimeStamp: Long = 0L,
) {
    fun getTimestampString(): String {
        val date = Date(currentTimeStamp)
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
}

fun List<Pairing>.addTime(): List<Pairing> {
    val currentTime = System.currentTimeMillis()
    return this.map {
        if (it.currentTimeStamp != 0L) it else
            it.copy(currentTimeStamp = currentTime)
    }
}
