package com.leviancode.android.gsmbox.utils

import androidx.room.TypeConverter
import com.leviancode.android.gsmbox.data.model.Recipient

class Converters {

    @TypeConverter
    fun stringFromRecipients(value: List<Recipient>): String {
        return value.joinToString(";"){ it.phoneNumber }
    }

    @TypeConverter
    fun recipientsFromString(value: String): MutableList<Recipient> {
        return value.split(";")
            .map{ Recipient (phoneNumber = it) }
            .toMutableList()
    }
}