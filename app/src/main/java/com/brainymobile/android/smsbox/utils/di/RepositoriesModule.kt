package com.brainymobile.android.smsbox.utils.di

import com.brainymobile.android.smsbox.data.repositories.*
import com.brainymobile.android.smsbox.domain.repositories.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoriesModule {

    @Binds
    abstract fun bindPlaceholdersRepository(
        placeholdersRepositoryImpl: PlaceholdersRepositoryImpl
    ): PlaceholdersRepository

    @Binds
    abstract fun bindTemplatesRepository(
        templatesRepositoryImpl: TemplatesRepositoryImpl
    ): TemplatesRepository

    @Binds
    abstract fun bindTemplateGroupsRepository(
        templateGroupsRepositoryImpl: TemplateGroupsRepositoryImpl
    ): TemplateGroupsRepository

    @Binds
    abstract fun bindRecipientsRepository(
        recipientsRepositoryImpl: RecipientsRepositoryImpl
    ): RecipientsRepository

    @Binds
    abstract fun bindRecipientGroupsRepository(
        recipientGroupsRepositoryImpl: RecipientGroupsRepositoryImpl
    ): RecipientGroupsRepository
}