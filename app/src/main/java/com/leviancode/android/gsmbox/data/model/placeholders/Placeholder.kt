package com.leviancode.android.gsmbox.data.model.placeholders

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.data.repository.PlaceholdersRepository
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.isNotNullOrEmpty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

@Entity(tableName = "placeholders")
data class Placeholder(
    @PrimaryKey(autoGenerate = true)
    var placeholderId: Long = 0L,
    private var name: String = "",
    private var value: String = ""
) : BaseObservable(), Serializable {

    @Bindable
    fun getName() = name
    fun setName(value: String) {
        if (name != value) {
            name = value
            notifyChange()
            checkNameIsUnique(value)
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
    fun isFieldsCorrect() = isNameUnique && isNotNullOrEmpty(name, value)

    private fun checkNameIsUnique(value: String){
        CoroutineScope(Dispatchers.IO).launch {
            val found = PlaceholdersRepository.getByName("#$value")
            isNameUnique = found == null || found.placeholderId == placeholderId
        }
    }
}
