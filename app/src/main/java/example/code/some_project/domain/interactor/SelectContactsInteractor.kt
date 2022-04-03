package example.code.some_project.domain.interactor

import io.reactivex.Single
import example.code.some_project.domain.models.Contact

interface SelectContactsInteractor {

    fun prepareData(): Single<SelectContactsData>
}

data class SelectContactsData(
    val contacts: List<Contact>
)