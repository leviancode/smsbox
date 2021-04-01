package com.leviancode.android.gsmbox.data.model.placeholders

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.utils.isNotEmpty
import java.io.Serializable
import java.util.*

@Entity(tableName = "placeholders")
data class Placeholder(
    @PrimaryKey
    var placeholderId: String = UUID.randomUUID().toString(),
    private var name: String = "",
    private var value: String = ""
) : BaseObservable(), Serializable {

    @Bindable
    fun getName() = name
    fun setName(value: String) {
        if (name != value) {
            name = value
            notifyChange()
        }
    }

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
    var isNameUnique = true
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun isFieldsCorrect() = isNameUnique && isNotEmpty(name, value)
}
