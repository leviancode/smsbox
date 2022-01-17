package com.brainymobile.android.smsbox.utils.di

import com.brainymobile.android.smsbox.data.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { AppDatabase.getInstance(androidApplication().applicationContext) }

    factory { get<AppDatabase>().templateDao() }
    factory { get<AppDatabase>().templateGroupDao() }
    factory { get<AppDatabase>().recipientDao() }
    factory { get<AppDatabase>().recipientGroupDao() }
    factory { get<AppDatabase>().placeholderDao() }
    factory { get<AppDatabase>().recipientAndGroupRelationDao() }
}