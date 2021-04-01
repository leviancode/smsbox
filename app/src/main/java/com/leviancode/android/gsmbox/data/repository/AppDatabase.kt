package com.leviancode.android.gsmbox.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leviancode.android.gsmbox.data.dao.*
import com.leviancode.android.gsmbox.data.model.placeholders.Placeholder
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.RecipientsAndGroupsCrossRef
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.utils.Converters
import com.leviancode.android.gsmbox.utils.DATABASE_NAME

@Database(entities = [
    Template::class,
    TemplateGroup::class,
    RecipientGroup::class,
    Recipient::class,
    RecipientsAndGroupsCrossRef::class,
    Placeholder::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun templateDao(): TemplateDao
    abstract fun templateGroupDao(): TemplateGroupDao
    abstract fun recipientDao(): RecipientDao
    abstract fun recipientGroupDao(): RecipientGroupDao
    abstract fun recipientsAndGroupsDao(): RecipientsAndGroupsCrossRefDao
    abstract fun placeholderDao(): PlaceholderDao

    companion object{
        lateinit var INSTANCE: AppDatabase

        fun init(context: Context){
            INSTANCE = Room.databaseBuilder(
                context,
                AppDatabase::class.java, DATABASE_NAME
            ).setJournalMode(JournalMode.TRUNCATE).build()
        }

        fun close(){
            if (INSTANCE.isOpen) INSTANCE.close()
        }
    }
}