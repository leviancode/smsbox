package com.leviancode.android.gsmbox.data.model.placeholders

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "placeholders")
data class Placeholder(
    @PrimaryKey
    var placeholderId: String = UUID.randomUUID().toString(),
    private var keyword: String = "",
    private var value: String = ""
) : BaseObservable(), Serializable {

    @Bindable
    fun getKeyword() = keyword
    fun setKeyword(value: String) {
        if (keyword != value) {
            keyword = value
            notifyChange()
        }
    }

    fun getKeyWithHashTag() = "#$keyword"

    @Bindable
    fun getValue() = value
    fun setValue(value: String) {
        if (this.value != value) {
            this.value = value
            notifyChange()
        }
    }

    @Ignore
    @get:Bindable
    var isKeyUnique = true
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun isFieldsCorrect() = isKeyUnique && value.isNotBlank()
}
