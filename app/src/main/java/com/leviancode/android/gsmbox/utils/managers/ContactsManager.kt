package com.leviancode.android.gsmbox.utils.managers

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientData
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI

class ContactsManager(private val context: Context) {

    fun parseUri(uri: Uri?): RecipientUI? {
        if (uri == null) return null
        var recipient: RecipientUI? = null
        val contentResolver = context.contentResolver

        contentResolver.query(
            uri, null, null, null, null
        )?.also { cur ->
            cur.moveToFirst()
            val idColumnIndex = cur.getColumnIndex(ContactsContract.Contacts._ID)
            val contactNameColumnIndex = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val id: String = if (idColumnIndex >= 0) cur.getString(idColumnIndex) else ""
            val contactName = if (contactNameColumnIndex >= 0) cur.getString(contactNameColumnIndex) else ""

            contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(id),
                null
            )?.also {
                while (it.moveToNext()) {
                    val phoneColumnIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val phone = if (phoneColumnIndex >= 0) it.getString(phoneColumnIndex) else ""

                    if (!phone.isNullOrBlank()) {
                        recipient = RecipientUI(name = contactName, phoneNumber = phone)
                        break
                    }
                }
            }?.close()
        }?.close()

        return recipient
    }
}