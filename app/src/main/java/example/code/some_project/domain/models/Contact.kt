package example.code.some_project.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import example.code.some_project.util.toBlacklistNumber

@Entity
data class Contact(
    @PrimaryKey
    val number: String,
    val name: String,
    val BlacklistNumber: String = number.toBlacklistNumber() // here for room
)