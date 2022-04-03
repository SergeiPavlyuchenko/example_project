package example.code.some_project.presentation.mvp.repo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import example.code.some_project.domain.repo.device.ContactPickerManager

// ToDo модифицировать под данный проект, если будет нужен. Если нет - удалить
class ContactPickerManagerImpl(private val context: Context) : ContactPickerManager {
    override fun selectContact(data: Intent): String {
        val contactData = data.data
        val cursor = dataQuery(contactData!!)
        var msisdn = ""
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val hasPhoneIndex =
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                val contactId = cursor.getString(idIndex)
                val hasPhone = cursor.getString(hasPhoneIndex)
                if (hasPhone == "1") {
                    val phones = phonesQuery(contactId)
                    if (phones != null) {
                        phones.moveToNext()
                        val phoneIndex =
                            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        msisdn = phones.getString(phoneIndex)
                        phones.close()
                    }
                }
            }
            cursor.close()
        }
        val regex = "[^0-9?!.]".toRegex()
        return msisdn.replace(regex, "")
    }

    private fun dataQuery(contactData: Uri) =
            contentResolver().query(contactData, null, null, null, null)

    private fun phonesQuery(contactId: String?) =
            contentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null)

    private fun contentResolver() = context.contentResolver
}