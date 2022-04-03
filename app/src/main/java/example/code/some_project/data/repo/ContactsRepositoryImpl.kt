package example.code.some_project.data.repo

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.data.db.DatabaseSource
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.ContactsRepository
import example.code.some_project.domain.repo.device.ContactsProvider
import example.code.some_project.util.Optional

class ContactsRepositoryImpl(
    private val database: DatabaseSource,
    private val contactsProvider: ContactsProvider
) : ContactsRepository {

    override fun getAllContacts(): Single<List<Contact>> = database.getAll()

    override fun searchContactsByName(query: String): Single<List<Contact>> =
        database.searchByName(query)

    override fun getContact(msisdn: String?): Single<Optional<Contact>> =
        if (msisdn.isNullOrBlank()) {
            Single.just(Optional.absent())
        } else {
            database.getByMsisdn(msisdn)
                .map { Optional.fromNullable(it) }
                .onErrorReturn { Optional.absent() }
        }

    override fun saveContact(contact: Contact): Completable = database.saveContact(contact)

    override fun removeContact(contact: Contact): Completable = database.removeContact(contact)

    override fun syncContacts(): Completable =
        contactsProvider.readContacts()
            .flatMapSingle { database.syncContact(it) }
            .ignoreElements()
}