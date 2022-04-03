package example.code.some_project.domain.interactor.implementation

import io.reactivex.Single
import example.code.some_project.domain.interactor.ContactsWithBlacklistInteractor
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.AuthRepo
import example.code.some_project.domain.repo.ContactsRepository

class ContactsWithBlacklistInteractorImpl(
    private val contactsRepository: ContactsRepository,
    private val authRepo: AuthRepo
) : ContactsWithBlacklistInteractor {

    override fun prepareData(): Single<ContactsWithBlacklist> =
        Single.zip(
            contactsRepository.getAllContacts(),
            authRepo.getUserMsisdn(),
            { contacts, selfMsisdn ->
                ContactsWithBlacklist(contacts.toMutableList(), selfMsisdn)
            }
        )
}

data class ContactsWithBlacklist(
    val contacts: MutableList<Contact>,
    val selfMsisdn: String
)