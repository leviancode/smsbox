package com.leviancode.android.gsmbox.utils

import android.Manifest
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.Recipient

object ContactsManager {
    fun openContactsApp(context: Context, launcher: ActivityResultLauncher<Void>) {

        Dexter.withContext(context)
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : BasePermissionListener() {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    launcher.launch(null)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(context, context.getString(R.string.permission_dined), Toast.LENGTH_SHORT).show()
                }
            }).check()
    }

    fun parseUri(context: Context, uri: Uri?): Recipient? {
        if (uri == null) return null
        val recipient = Recipient()
        val contentResolver = context.contentResolver

        contentResolver.query(
            uri, null, null, null, null
        )?.also { cur ->
            cur.moveToFirst()
            val id: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
            recipient.recipientName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

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
                        recipient.phoneNumber = phone
                        break
                    }
                }
            }?.close()
        }?.close()

        return recipient
    }
}