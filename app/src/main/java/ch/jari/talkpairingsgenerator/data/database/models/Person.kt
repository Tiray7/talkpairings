package ch.jari.talkpairingsgenerator.data.database.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Person(
    @PrimaryKey(autoGenerate = false)
    val personName: String,
    val friendName: String = personName,
    val selected: Boolean = false,
)


@Entity(
    primaryKeys = ["personName", "friendName"],
    indices = [Index(value = ["friendName"])]
)
data class PersonCrossRef(
    val personName: String,
    val friendName: String,
)

data class PersonWithFriends(
    @Embedded
    val person: Person,
    @Relation(
        parentColumn = "personName",
        entityColumn = "friendName",
        associateBy = Junction(PersonCrossRef::class)
    )
    val friends: List<Person>,
)
