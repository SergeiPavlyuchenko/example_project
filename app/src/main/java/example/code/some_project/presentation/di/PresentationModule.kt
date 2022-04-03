package example.code.some_project.presentation.di

import example.code.some_project.presentation.ui.activity.auth.AbstractAuthActivityPresenter
import example.code.some_project.presentation.ui.activity.auth.AuthActivityPresenterImpl
import example.code.some_project.presentation.ui.activity.main.AbstractMainActivityPresenter
import example.code.some_project.presentation.ui.activity.main.MainActivityPresenterImpl
import example.code.some_project.presentation.ui.activity.onboarding.AbstractOnboardingActivityPresenter
import example.code.some_project.presentation.ui.activity.onboarding.OnboardingActivityPresenterImpl
import example.code.some_project.presentation.ui.activity.splash.AbstractSplashPresenter
import example.code.some_project.presentation.ui.activity.splash.SplashPresenterImpl
import example.code.some_project.presentation.ui.fragment.about_product.AboutProductPresenterImpl
import example.code.some_project.presentation.ui.fragment.about_product.AbstractAboutProductPresenter
import example.code.some_project.presentation.ui.fragment.contacts.AbstractContactsPresenter
import example.code.some_project.presentation.ui.fragment.contacts.ContactsPresenterImpl
import example.code.some_project.presentation.ui.fragment.contactssearch.AbstractContactsSearchPresenter
import example.code.some_project.presentation.ui.fragment.contactssearch.ContactsSearchPresenterImpl
import example.code.some_project.presentation.ui.fragment.editcontact.AbstractEditContactPresenter
import example.code.some_project.presentation.ui.fragment.editcontact.EditContactPresenterImpl
import example.code.some_project.presentation.ui.fragment.manage_subscription.AbstractManageSubscriptionPresenter
import example.code.some_project.presentation.ui.fragment.manage_subscription.ManageSubscriptionPresenterImpl
import example.code.some_project.presentation.ui.fragment.privacy_policy.AbstractPrivacyPolicyPresenter
import example.code.some_project.presentation.ui.fragment.privacy_policy.PrivacyPolicyPresenterImpl
import example.code.some_project.presentation.ui.fragment.profile.AbstractProfilePresenter
import example.code.some_project.presentation.ui.fragment.profile.ProfilePresenterImpl
import org.koin.dsl.module

val screensModule = module {
    factory<AbstractSplashPresenter> {
        SplashPresenterImpl(get(), get())
    }
    factory<AbstractMainActivityPresenter> {
        MainActivityPresenterImpl(get())
    }
    factory<AbstractOnboardingActivityPresenter> {
        OnboardingActivityPresenterImpl(get(), get())
    }
    factory<AbstractAuthActivityPresenter> {
        AuthActivityPresenterImpl(get(), get(), get())
    }

    factory<AbstractContactsPresenter> {
        ContactsPresenterImpl(get(), get(), get(), get(), get(), get())
    }
    factory<AbstractEditContactPresenter> { EditContactPresenterImpl(get(), get(), get(), get(), get()) }

    factory<AbstractContactsSearchPresenter> {
        ContactsSearchPresenterImpl(get(), get(), get(), get())
    }
    factory<AbstractAboutProductPresenter> { AboutProductPresenterImpl(get(), get(), get()) }
    factory<AbstractPrivacyPolicyPresenter> { PrivacyPolicyPresenterImpl(get(), get()) }
    factory<AbstractProfilePresenter> { ProfilePresenterImpl(get(), get(), get(), get(), get()) }

    factory<AbstractManageSubscriptionPresenter> {
        ManageSubscriptionPresenterImpl(get(), get(), get())
    }
}