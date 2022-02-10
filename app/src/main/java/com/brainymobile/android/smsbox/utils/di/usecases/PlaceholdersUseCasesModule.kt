package com.brainymobile.android.smsbox.utils.di.usecases

import com.brainymobile.android.smsbox.domain.usecases.placeholders.DeletePlaceholdersUseCase
import com.brainymobile.android.smsbox.domain.usecases.placeholders.FetchPlaceholdersUseCase
import com.brainymobile.android.smsbox.domain.usecases.placeholders.ReplacePlaceholdersUseCase
import com.brainymobile.android.smsbox.domain.usecases.placeholders.SavePlaceholdersUseCase
import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.DeletePlaceholdersUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.FetchPlaceholdersUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.ReplacePlaceholdersUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.SavePlaceholdersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class PlaceholdersUseCasesModule {

    @Binds
    abstract fun bindFetchPlaceholdersUseCase(
        fetchPlaceholdersUseCaseImpl: FetchPlaceholdersUseCaseImpl
    ): FetchPlaceholdersUseCase

    @Binds
    abstract fun bindSavePlaceholdersUseCase(
        savePlaceholdersUseCaseImpl: SavePlaceholdersUseCaseImpl
    ): SavePlaceholdersUseCase

    @Binds
    abstract fun bindReplacePlaceholdersUseCase(
        replacePlaceholdersUseCaseImpl: ReplacePlaceholdersUseCaseImpl
    ): ReplacePlaceholdersUseCase

    @Binds
    abstract fun bindDeletePlaceholdersUseCase(
        deletePlaceholdersUseCaseImpl: DeletePlaceholdersUseCaseImpl
    ): DeletePlaceholdersUseCase
}