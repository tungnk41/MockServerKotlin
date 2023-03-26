package com.mock.client.di

import com.mock.client.Script
import com.mock.client.TokenManager
import com.mock.client.network.ApiNetworkService
import com.mock.client.network.AuthNetworkService
import com.mock.client.network.impl.ApiNetworkServiceImpl
import com.mock.client.network.impl.AuthNetworkServiceImpl
import org.koin.dsl.module


val clientNetworkModule = module {
    single { TokenManager() }
    single<AuthNetworkService> { AuthNetworkServiceImpl(get()) }
    single<ApiNetworkService> { ApiNetworkServiceImpl(get(), get()) }

    single { Script() }
}