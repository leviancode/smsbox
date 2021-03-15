package com.leviancode.android.gsmbox.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leviancode.android.gsmbox.data.dao.RecipientDao
import com.leviancode.android.gsmbox.data.dao.RecipientGroupDao
import com.leviancode.android.gsmbox.data.dao.TemplateDao
import com.leviancode.android.gsmbox.data.dao.TemplateGroupDao
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.utils.Converters
import com.leviancode.android.gsmbox.utils.DATABASE_NAME

@Database(entities = [Template::class, TemplateGroup::class, RecipientGroup::class, Recipient::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun templateDao(): TemplateDao
    abstract fun templateGroupDao(): TemplateGroupDao
    abstract fun recipientDao(): RecipientDao
    abstract fun recipientGroupDao(): RecipientGroupDao

    companion object{
        lateinit var INSTANCE: AppDatabase

        fun init(context: Context){
            INSTANCE = Room.databaseBuilder(
                context,
                AppDatabase::class.java, DATABASE_NAME
            ).build()
        }
    }
}