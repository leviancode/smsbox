package com.leviancode.android.gsmbox.utils

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun stringFromStringList(value: List<String>): String {
        return value.joinToString(";")
    }

    @TypeConverter
    fun stringListFromString(value: String): MutableList<String> {
        return value.split(";").toMutableList()
    }
}