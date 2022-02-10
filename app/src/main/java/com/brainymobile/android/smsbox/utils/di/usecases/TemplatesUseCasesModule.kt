package com.brainymobile.android.smsbox.utils.di.usecases

import com.brainymobile.android.smsbox.domain.usecases.templates.groups.DeleteTemplateGroupUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.FetchTemplateGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.SaveTemplateGroupUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.UpdateTemplateGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl.DeleteTemplateGroupUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl.FetchTemplateGroupsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl.SaveTemplateGroupUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl.UpdateTemplateGroupsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.DeleteTemplatesUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.SaveTemplatesUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.UpdateTemplateUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl.DeleteTemplatesUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl.FetchTemplatesUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl.SaveTemplatesUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl.UpdateTemplateUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class TemplatesUseCasesModule {

    @Binds
    abstract fun bindFetchTemplatesUseCase(
        fetchTemplatesUseCaseImpl: FetchTemplatesUseCaseImpl
    ): FetchTemplatesUseCase

    @Binds
    abstract fun bindSaveTemplatesUseCase(
        saveTemplatesUseCaseImpl: SaveTemplatesUseCaseImpl
    ): SaveTemplatesUseCase

    @Binds
    abstract fun bindUpdateTemplateUseCase(
        updateTemplateUseCaseImpl: UpdateTemplateUseCaseImpl
    ): UpdateTemplateUseCase

    @Binds
    abstract fun bindDeleteTemplatesUseCase(
        deleteTemplatesUseCaseImpl: DeleteTemplatesUseCaseImpl
    ): DeleteTemplatesUseCase

    @Binds
    abstract fun bindFetchTemplateGroupsUseCase(
        fetchTemplateGroupsUseCaseImpl: FetchTemplateGroupsUseCaseImpl
    ): FetchTemplateGroupsUseCase

    @Binds
    abstract fun bindSaveTemplateGroupUseCase(
        saveTemplateGroupUseCaseImpl: SaveTemplateGroupUseCaseImpl
    ): SaveTemplateGroupUseCase

    @Binds
    abstract fun bindUpdateTemplateGroupsUseCase(
        updateTemplateGroupsUseCaseImpl: UpdateTemplateGroupsUseCaseImpl
    ): UpdateTemplateGroupsUseCase

    @Binds
    abstract fun bindDeleteTemplateGroupUseCase(
        deleteTemplateGroupUseCaseImpl: DeleteTemplateGroupUseCaseImpl
    ): DeleteTemplateGroupUseCase
}