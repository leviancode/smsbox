package com.leviancode.android.gsmbox.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leviancode.android.gsmbox.data.database.dao.*
import com.leviancode.android.gsmbox.data.database.dao.recipients.RecipientAndGroupRelationDao
import com.leviancode.android.gsmbox.data.database.dao.recipients.RecipientDao
import com.leviancode.android.gsmbox.data.database.dao.recipients.RecipientGroupDao
import com.leviancode.android.gsmbox.data.database.dao.templates.TemplateDao
import com.leviancode.android.gsmbox.data.database.dao.templates.TemplateGroupDao
import com.leviancode.android.gsmbox.data.entities.placeholders.PlaceholderData
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientData
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientGroupData
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientsAndGroupRelation
import com.leviancode.android.gsmbox.data.entities.templates.template.TemplateData
import com.leviancode.android.gsmbox.data.entities.templates.group.TemplateGroupData
import com.leviancode.android.gsmbox.utils.DATABASE_NAME

@Database(entities = [
    TemplateData::class,
    TemplateGroupData::class,
    RecipientGroupData::class,
    RecipientData::class,
    RecipientsAndGroupRelation::class,
    PlaceholderData::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun templateDao(): TemplateDao
    abstract fun templateGroupDao(): TemplateGroupDao
    abstract fun recipientDao(): RecipientDao
    abstract fun recipientGroupDao(): RecipientGroupDao
    abstract fun placeholderDao(): PlaceholderDao
    abstract fun recipientAndGroupRelationDao(): RecipientAndGroupRelationDao

    companion object{
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return if (instance == null){
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, DATABASE_NAME
                ).setJournalMode(JournalMode.TRUNCATE)
                    .allowMainThreadQueries()
                    .build()
            } else instance!!
        }

        fun close(){
            instance?.let {
                if (it.isOpen) it.close()
                instance = null
            }
        }
    }
}