package example.code.some_project.domain.interactor

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.models.Contact
import example.code.some_project.util.Optional

interface EditContactInteractor {

    fun loadContact(msisdn: String?): Single<Optional<Contact>>

    fun saveContact(originalContact: Contact?, newContact: Contact): Completable
}