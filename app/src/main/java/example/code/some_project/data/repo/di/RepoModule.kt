package example.code.some_project.data.repo.di

import example.code.some_project.data.repo.*
import example.code.some_project.domain.repo.*
import org.koin.dsl.module

val repoModule = module {
    single<AuthRepo> { AuthRepoImpl(get(), get(), get()) }
    single<SettingsRepo> { SettingsRepoImpl(get(), get()) }
    single<FirebaseRepo> { FirebaseRepoImpl(get()) }
    single<ContactsRepository> { ContactsRepositoryImpl(get(), get()) }
    single<SubscriptionRepo> { SubscriptionRepoIml(get(), get()) }
    single<UserParametersRepo> { UserParametersRepoImpl(get(), get()) }
}
