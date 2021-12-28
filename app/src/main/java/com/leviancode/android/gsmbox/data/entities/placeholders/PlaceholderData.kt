package com.leviancode.android.gsmbox.data.entities.placeholders

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "placeholders",
    indices = [Index(value = ["name"], unique = true)])
data class PlaceholderData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val position: Int,
    val name: String,
    val value: String,
    val timestamp: Long = System.currentTimeMillis()
)
