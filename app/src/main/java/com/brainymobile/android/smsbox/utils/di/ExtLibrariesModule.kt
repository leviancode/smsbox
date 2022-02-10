package com.brainymobile.android.smsbox.utils.di

import com.yariksoffice.lingver.Lingver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object ExtLibrariesModule {

    @Provides
    fun provideLingver(): Lingver = Lingver.getInstance()
}