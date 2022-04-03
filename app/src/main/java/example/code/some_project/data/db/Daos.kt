package example.code.some_project.data.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.models.Contact

@Dao
interface ContactsDao {

    @Query("SELECT * FROM contact ORDER BY upper(name)")
    fun getAll(): Single<List<Contact>>

    @Query("SELECT * FROM contact WHERE number = :msisdn")
    fun getByMsisdn(msisdn: String): Single<Contact>

    @Query("SELECT * FROM contact WHERE lower(name) LIKE '%' || lower(:query) || '%'")
    fun searchByName(query: String): Single<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: Contact): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun syncContact(contact: Contact)

    @Delete
    fun removeContact(contact: Contact): Completable
}