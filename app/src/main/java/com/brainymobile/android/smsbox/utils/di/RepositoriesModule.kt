package com.brainymobile.android.smsbox.utils.di

import com.brainymobile.android.smsbox.data.repositories.*
import org.koin.dsl.module

val repositoriesModule = module {
    factory { PlaceholdersRepositoryImpl(get()) }
    factory { TemplatesRepositoryImpl(get(),get<RecipientGroupsRepositoryImpl>()) }
    factory { TemplateGroupsRepositoryImpl(get(), get<TemplatesRepositoryImpl>()) }
    factory { RecipientsRepositoryImpl(get(),get(), get()) }
    factory { RecipientGroupsRepositoryImpl(get(),get(), get()) }
}