package example.code.some_project.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import example.code.some_project.domain.models.Contact

private const val VERSION_FIRST = 1

@Database(entities = [Contact::class], version = VERSION_FIRST)
abstract class Database : RoomDatabase() {

    abstract fun getContactsDao(): ContactsDao
}