package com.brainymobile.android.smsbox.data.database

import androidx.room.TypeConverter
import com.brainymobile.android.smsbox.data.entities.recipients.RecipientData

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