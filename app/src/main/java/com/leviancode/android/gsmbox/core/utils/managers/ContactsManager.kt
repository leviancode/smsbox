package com.leviancode.android.gsmbox.core.utils.managers

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.leviancode.android.gsmbox.core.data.model.recipients.Recipient

object ContactsManager {

    fun parseUri(context: Context, uri: Uri?): Recipient? {
        if (uri == null) return null
        var recipient: Recipient? = null
        val contentResolver = context.contentResolver

        contentResolver.query(
            uri, null, null, null, null
        )?.also { cur ->
            cur.moveToFirst()
            val id: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
            val contactName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

            contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(id),
                null
            )?.also {
                while (it.moveToNext()) {
                    val phone = it.getString(
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )
                    if (!phone.isNullOrBlank()) {
                        recipient = Recipient(name = contactName, phoneNumber = phone)
                        break
                    }
                }
            }?.close()
        }?.close()

        return recipient
    }
}