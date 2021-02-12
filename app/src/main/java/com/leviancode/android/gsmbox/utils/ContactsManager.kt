package com.leviancode.android.gsmbox.utils

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

object ContactsManager {
    fun getPhoneNumberByContactUri(context: Context, uri: Uri?): String? {
        if (uri == null) return null
        var phoneNumber: String? = null
        val contentResolver = context.contentResolver

        contentResolver.query(
            uri, null, null, null, null
        )?.also { cur ->
            cur.moveToFirst()
            val id: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))

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
                        phoneNumber = phone
                        break
                    }
                }
            }?.close()
        }?.close()

        return phoneNumber
    }
}