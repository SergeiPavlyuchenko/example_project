package example.code.some_project.domain.repo

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.models.Contact
import example.code.some_project.util.Optional

interface ContactsRepository {

    fun getAllContacts(): Single<List<Contact>>

    fun searchContactsByName(query: String): Single<List<Contact>>

    fun getContact(msisdn: String?): Single<Optional<Contact>>

    fun saveContact(contact: Contact): Completable

    fun removeContact(contact: Contact): Completable

    fun syncContacts(): Completable
}