package example.code.some_project.data.local

import android.content.Context
import android.provider.ContactsContract
import io.reactivex.Observable
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.device.ContactsProvider

class ContactsProviderImpl(private val context: Context) : ContactsProvider {

    override fun readContacts(): Observable<Contact> =
        Observable.create { emitter ->
            val resolver = context.contentResolver
            val contactsCursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

            if (contactsCursor != null && contactsCursor.count > 0) {
                val idIndex = contactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                val nameIndex =
                    contactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)
                val hasPhoneNumberIndex =
                    contactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                while (contactsCursor.moveToNext()) {
                    if (emitter.isDisposed) {
                        break
                    }

                    val id = contactsCursor.getString(idIndex)
                    val name = contactsCursor.getString(nameIndex)

                    if (contactsCursor.getString(hasPhoneNumberIndex).toInt() > 0) {
                        val numbersCursor = resolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                            arrayOf(id),
                            null
                        )

                        if (numbersCursor != null) {
                            val phoneIndex = numbersCursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )

                            while (numbersCursor.moveToNext()) {
                                if (emitter.isDisposed) {
                                    break
                                }

                                val number = numbersCursor.getString(phoneIndex)

                                if (!emitter.isDisposed) {
                                    emitter.onNext(Contact(number, name))
                                }
                            }

                            numbersCursor.close()
                        }
                    }
                }

                contactsCursor.close()
            }

            if (!emitter.isDisposed) {
                emitter.onComplete()
            }
        }

}