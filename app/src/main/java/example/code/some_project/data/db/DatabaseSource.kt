package example.code.some_project.data.db

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.models.Contact

interface DatabaseSource {

    fun getAll(): Single<List<Contact>>

    fun searchByName(query: String): Single<List<Contact>>

    fun getByMsisdn(msisdn: String): Single<Contact>

    fun saveContact(contact: Contact): Completable

    fun syncContact(contact: Contact): Single<Boolean>

    fun removeContact(contact: Contact): Completable
}