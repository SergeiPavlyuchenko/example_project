package example.code.some_project.domain.repo.device

import io.reactivex.Observable
import example.code.some_project.domain.models.Contact

interface ContactsProvider {

    fun readContacts(): Observable<Contact>
}