package example.code.some_project.domain.interactor.implementation

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.interactor.EditContactInteractor
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.ContactsRepository
import example.code.some_project.util.Optional

class EditContactInteractorImpl(
    private val contactsRepository: ContactsRepository
) : EditContactInteractor {

    override fun loadContact(msisdn: String?): Single<Optional<Contact>> =
        contactsRepository.getContact(msisdn)

    override fun saveContact(originalContact: Contact?, newContact: Contact): Completable =
        if (originalContact == null) {
            contactsRepository.saveContact(newContact)
        } else {
            contactsRepository.removeContact(originalContact)
                .andThen(contactsRepository.saveContact(newContact))
        }
}