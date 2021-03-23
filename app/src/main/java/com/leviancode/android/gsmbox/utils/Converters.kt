package com.leviancode.android.gsmbox.utils

import androidx.room.TypeConverter
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup

class Converters {

    @TypeConverter
    fun stringFromRecipients(value: List<Recipient>): String {
        return value.filter{ it.getPhoneNumber().isNotBlank() }
                    .joinToString(";"){ it.getPhoneNumber() }
    }

    @TypeConverter
    fun recipientsFromString(value: String): MutableList<Recipient> {
        return value.split(";")
                    .map{ Recipient (phoneNumber = it) }
                    .toMutableList()
    }
}