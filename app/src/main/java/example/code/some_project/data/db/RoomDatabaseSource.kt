package example.code.some_project.data.db

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.models.Contact

class RoomDatabaseSource(private val database: Database) : DatabaseSource {

    override fun getAll(): Single<List<Contact>> = database.getContactsDao().getAll()

    override fun searchByName(query: String): Single<List<Contact>> =
        database.getContactsDao().searchByName(query)

    override fun getByMsisdn(msisdn: String): Single<Contact> =
        database.getContactsDao().getByMsisdn(msisdn)

    override fun saveContact(contact: Contact): Completable =
        database.getContactsDao().insertContact(contact)

    override fun syncContact(contact: Contact): Single<Boolean> =
        Single.just(contact)
            .doOnSuccess {
                database.getContactsDao().syncContact(it)
            }
            .map { true } // ToDo validation


    override fun removeContact(contact: Contact): Completable =
        database.getContactsDao().removeContact(contact)
}