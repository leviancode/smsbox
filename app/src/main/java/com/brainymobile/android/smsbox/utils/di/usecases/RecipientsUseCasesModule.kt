package com.brainymobile.android.smsbox.utils.di.usecases

import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.DeleteRecipientGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.FetchRecipientGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.SaveRecipientGroupsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.impl.DeleteRecipientGroupsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.impl.FetchRecipientGroupsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.impl.SaveRecipientGroupsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.DeleteRecipientsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.FetchRecipientsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.SaveRecipientsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.UpdateRecipientsUseCase
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl.DeleteRecipientsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl.FetchRecipientsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl.SaveRecipientsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl.UpdateRecipientsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RecipientsUseCasesModule {

    @Binds
    abstract fun bindFetchRecipientsUseCase(
        fetchRecipientsUseCaseImpl: FetchRecipientsUseCaseImpl
    ): FetchRecipientsUseCase

    @Binds
    abstract fun bindSaveRecipientsUseCase(
        saveRecipientsUseCaseImpl: SaveRecipientsUseCaseImpl
    ): SaveRecipientsUseCase

    @Binds
    abstract fun bindUpdateRecipientsUseCase(
        updateRecipientsUseCaseImpl: UpdateRecipientsUseCaseImpl
    ): UpdateRecipientsUseCase

    @Binds
    abstract fun bindDeleteRecipientsUseCase(
        deleteRecipientsUseCaseImpl: DeleteRecipientsUseCaseImpl
    ): DeleteRecipientsUseCase

    @Binds
    abstract fun bindFetchRecipientGroupsUseCase(
        fetchRecipientGroupsUseCaseImpl: FetchRecipientGroupsUseCaseImpl
    ): FetchRecipientGroupsUseCase

    @Binds
    abstract fun bindSaveRecipientGroupsUseCase(
        saveRecipientGroupsUseCaseImpl: SaveRecipientGroupsUseCaseImpl
    ): SaveRecipientGroupsUseCase

    @Binds
    abstract fun bindDeleteRecipientGroupsUseCase(
        deleteRecipientGroupsUseCaseImpl: DeleteRecipientGroupsUseCaseImpl
    ): DeleteRecipientGroupsUseCase
}