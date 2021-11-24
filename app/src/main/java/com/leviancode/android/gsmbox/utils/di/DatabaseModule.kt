package com.leviancode.android.gsmbox.utils.di

import com.leviancode.android.gsmbox.data.database.AppDatabase
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