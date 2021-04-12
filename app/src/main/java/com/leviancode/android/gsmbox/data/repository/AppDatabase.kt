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
import com.leviancode.android.gsmbox.data.model.recipients.RecipientsAndGroupRelation
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.model.templates.TemplateAndRecipientRelation
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.utils.Converters
import com.leviancode.android.gsmbox.utils.DATABASE_NAME

@Database(entities = [
    Template::class,
    TemplateGroup::class,
    RecipientGroup::class,
    Recipient::class,
    RecipientsAndGroupRelation::class,
    TemplateAndRecipientRelation::class,
    Placeholder::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun templateDao(): TemplateDao
    abstract fun templateGroupDao(): TemplateGroupDao
    abstract fun templateAndRecipientsDao(): TemplateAndRecipientRelationDao
    abstract fun recipientDao(): RecipientDao
    abstract fun recipientGroupDao(): RecipientGroupDao
    abstract fun recipientsAndGroupsDao(): RecipientsAndGroupRelationDao
    abstract fun placeholderDao(): PlaceholderDao

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