package com.leviancode.android.gsmbox.core.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leviancode.android.gsmbox.core.data.dao.*
import com.leviancode.android.gsmbox.core.data.dao.recipients.RecipientAndGroupRelationDao
import com.leviancode.android.gsmbox.core.data.dao.recipients.RecipientDao
import com.leviancode.android.gsmbox.core.data.dao.recipients.RecipientGroupDao
import com.leviancode.android.gsmbox.core.data.dao.templates.TemplateDao
import com.leviancode.android.gsmbox.core.data.dao.templates.TemplateGroupDao
import com.leviancode.android.gsmbox.core.data.model.placeholders.Placeholder
import com.leviancode.android.gsmbox.core.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.core.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.core.data.model.recipients.RecipientsAndGroupRelation
import com.leviancode.android.gsmbox.core.data.model.templates.Template
import com.leviancode.android.gsmbox.core.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.core.utils.DATABASE_NAME

@Database(entities = [
    Template::class,
    TemplateGroup::class,
    RecipientGroup::class,
    Recipient::class,
    RecipientsAndGroupRelation::class,
    Placeholder::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun templateDao(): TemplateDao
    abstract fun templateGroupDao(): TemplateGroupDao
    abstract fun recipientDao(): RecipientDao
    abstract fun recipientGroupDao(): RecipientGroupDao
    abstract fun placeholderDao(): PlaceholderDao
    abstract fun recipientAndGroupRelationDao(): RecipientAndGroupRelationDao

    companion object{
        lateinit var instance: AppDatabase

        fun init(context: Context){
            instance = Room.databaseBuilder(
                context,
                AppDatabase::class.java, DATABASE_NAME
            ).setJournalMode(JournalMode.TRUNCATE).build()
        }

        fun close(){
            if (instance.isOpen) instance.close()
        }
    }
}