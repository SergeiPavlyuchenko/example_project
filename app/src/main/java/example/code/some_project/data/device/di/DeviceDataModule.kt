package example.code.some_project.data.device.di

import example.code.some_project.data.device.CookiesCleanerImpl
import example.code.some_project.data.device.DeviceManagerImpl
import example.code.some_project.data.device.NetworkManagerImpl
import example.code.some_project.domain.repo.device.ContactPickerManager
import example.code.some_project.domain.repo.device.CookiesCleaner
import example.code.some_project.domain.repo.device.DeviceManager
import example.code.some_project.domain.repo.device.ResourcesManager
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.repo.ContactPickerManagerImpl
import example.code.some_project.presentation.mvp.repo.ResourceManagerImpl
import org.koin.dsl.module

val deviceModule = module {
    single<NetworkManager> { NetworkManagerImpl(context = get()) }
    single<ResourcesManager> { ResourceManagerImpl(context = get()) }
    single<ContactPickerManager> { ContactPickerManagerImpl(context = get()) }
    single<CookiesCleaner> { CookiesCleanerImpl() }
    single<DeviceManager> { DeviceManagerImpl(context = get()) }
}