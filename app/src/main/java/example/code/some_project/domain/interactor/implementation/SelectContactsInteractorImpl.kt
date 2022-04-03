package example.code.some_project.domain.interactor.implementation

import example.code.some_project.domain.extensions.justSingle
import io.reactivex.Single
import example.code.some_project.domain.interactor.SelectContactsData
import example.code.some_project.domain.interactor.SelectContactsInteractor
import example.code.some_project.domain.repo.ContactsRepository

class SelectContactsInteractorImpl(
    private val contactsRepository: ContactsRepository
) : SelectContactsInteractor {

    override fun prepareData(): Single<SelectContactsData> =
        contactsRepository.getAllContacts()
            .flatMap { SelectContactsData(it).justSingle() }

}