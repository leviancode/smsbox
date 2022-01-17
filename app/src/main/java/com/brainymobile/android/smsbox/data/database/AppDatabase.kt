package com.brainymobile.android.smsbox.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.brainymobile.android.smsbox.data.database.dao.placeholders.PlaceholderDao
import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientAndGroupRelationDao
import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientDao
import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientGroupDao
import com.brainymobile.android.smsbox.data.database.dao.templates.TemplateDao
import com.brainymobile.android.smsbox.data.database.dao.templates.TemplateGroupDao
import com.brainymobile.android.smsbox.data.entities.placeholders.PlaceholderData
import com.brainymobile.android.smsbox.data.entities.recipients.RecipientData
import com.brainymobile.android.smsbox.data.entities.recipients.RecipientGroupData
import com.brainymobile.android.smsbox.data.entities.recipients.RecipientsAndGroupRelation
import com.brainymobile.android.smsbox.data.entities.templates.group.TemplateGroupData
import com.brainymobile.android.smsbox.data.entities.templates.template.TemplateData
import com.brainymobile.android.smsbox.utils.DATABASE_NAME

@Database(
    entities = [
        TemplateData::class,
        TemplateGroupData::class,
        RecipientGroupData::class,
        RecipientData::class,
        RecipientsAndGroupRelation::class,
        PlaceholderData::class],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
    abstract fun templateGroupDao(): TemplateGroupDao
    abstract fun recipientDao(): RecipientDao
    abstract fun recipientGroupDao(): RecipientGroupDao
    abstract fun placeholderDao(): PlaceholderDao
    abstract fun recipientAndGroupRelationDao(): RecipientAndGroupRelationDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return if (instance == null) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, DATABASE_NAME
                ).setJournalMode(JournalMode.TRUNCATE)
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_3_4)
                    .build()
            } else instance!!
        }

        fun close() {
            instance?.let {
                if (it.isOpen) it.close()
                instance = null
            }
        }
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE template_groups ADD COLUMN 'position' INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE templates ADD COLUMN 'position' INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE placeholders ADD COLUMN 'position' INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE recipients ADD COLUMN 'position' INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE recipient_groups ADD COLUMN 'position' INTEGER NOT NULL DEFAULT 0")
    }
}