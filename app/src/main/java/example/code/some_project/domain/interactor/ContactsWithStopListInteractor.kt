package example.code.some_project.domain.interactor

import io.reactivex.Single
import example.code.some_project.domain.interactor.implementation.ContactsWithBlacklist

interface ContactsWithBlacklistInteractor {

    fun prepareData(): Single<ContactsWithBlacklist>
}
