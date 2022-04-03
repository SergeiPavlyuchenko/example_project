package example.code.some_project.domain.interactor.di

import example.code.some_project.domain.interactor.*
import example.code.some_project.domain.interactor.implementation.*
import org.koin.dsl.module

val interactorModule = module {
    factory<AuthInteractor> { AuthInteractorImpl(get(), get()) }
    factory<OnboardingInteractor> { OnboardingInteractorImpl(get(), get()) }
    factory<SplashInteractor> {
        SplashInteractorImpl(get(), get())
    }
    single<ContactsWithBlacklistInteractor> { ContactsWithBlacklistInteractorImpl(get(), get()) }
    single<ContactsInteractor> { ContactsInteractorImpl(get(), get()) }
    single<EditContactInteractor> { EditContactInteractorImpl(get()) }
    single<SubscriptionInteractor> { SubscriptionInteractorImpl(get(), get()) }
    single<SelectContactsInteractor> { SelectContactsInteractorImpl(get()) }
    single<ProfileInteractor> { ProfileInteractorImpl(get(), get(), get()) }
    single<RateAppInteractor> { RateAppInteractorImpl(get()) }
    single<LogoutInteractor> { LogoutInteractorImpl(get()) }
}