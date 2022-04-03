package example.code.some_project.network.di

import example.code.some_project.network.ServiceFactory
import org.koin.dsl.module

val networkModule = module {
    single { ServiceFactory(authDataSourceLocal = get()) }
    single { get<ServiceFactory>().getAuthApiService() }
    single { get<ServiceFactory>().getSubscriptionApiService() }
    single { get<ServiceFactory>().getUserParametersService() }
}