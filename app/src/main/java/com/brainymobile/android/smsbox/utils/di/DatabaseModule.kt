package com.brainymobile.android.smsbox.utils.di

import android.content.Context
import com.brainymobile.android.smsbox.data.database.AppDatabase
import com.brainymobile.android.smsbox.data.database.dao.placeholders.PlaceholderDao
import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientAndGroupRelationDao
import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientDao
import com.brainymobile.android.smsbox.data.database.dao.recipients.RecipientGroupDao
import com.brainymobile.android.smsbox.data.database.dao.templates.TemplateDao
import com.brainymobile.android.smsbox.data.database.dao.templates.TemplateGroupDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideTemplateDao(appDatabase: AppDatabase): TemplateDao {
        return appDatabase.templateDao()
    }

    @Provides
    fun provideTemplateGroupDao(appDatabase: AppDatabase): TemplateGroupDao {
        return appDatabase.templateGroupDao()
    }

    @Provides
    fun provideRecipientDao(appDatabase: AppDatabase): RecipientDao {
        return appDatabase.recipientDao()
    }

    @Provides
    fun provideRecipientGroupDao(appDatabase: AppDatabase): RecipientGroupDao {
        return appDatabase.recipientGroupDao()
    }

    @Provides
    fun providePlaceholderDao(appDatabase: AppDatabase): PlaceholderDao {
        return appDatabase.placeholderDao()
    }

    @Provides
    fun provideRecipientAndGroupRelationDao(appDatabase: AppDatabase): RecipientAndGroupRelationDao {
        return appDatabase.recipientAndGroupRelationDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }
}