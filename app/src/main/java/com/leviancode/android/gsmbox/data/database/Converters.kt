package com.leviancode.android.gsmbox.data.database

import androidx.room.TypeConverter
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientData

class Converters {

    @TypeConverter
    fun stringFromRecipients(value: List<RecipientData>): String {
        return value.filter{ it.phoneNumber.isNotBlank() }
                    .joinToString(";"){ it.phoneNumber }
    }

    @TypeConverter
    fun recipientsFromString(value: String): MutableList<RecipientData> {
        return value.split(";")
                    .map{ RecipientData (phoneNumber = it) }
                    .toMutableList()
    }
}