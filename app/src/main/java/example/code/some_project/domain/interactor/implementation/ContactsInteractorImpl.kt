package example.code.some_project.domain.interactor.implementation

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.interactor.ContactsInteractor
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.AuthRepo
import example.code.some_project.domain.repo.ContactsRepository


class ContactsInteractorImpl(
    private val contactsRepository: ContactsRepository,
    private val authRepo: AuthRepo
) : ContactsInteractor {

    override fun syncContacts(): Single<List<Contact>> =
        contactsRepository.syncContacts()
            .andThen(contactsRepository.getAllContacts())

    override fun removeContact(contact: Contact): Completable =
        contactsRepository.removeContact(contact)

    override fun getUserMsisdn(): Single<String> = authRepo.getUserMsisdn()

}