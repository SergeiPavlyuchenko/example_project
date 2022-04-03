package example.code.some_project.domain.interactor

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.models.Contact

interface ContactsInteractor {

    fun syncContacts(): Single<List<Contact>>

    fun removeContact(contact: Contact): Completable

    fun getUserMsisdn(): Single<String>
}