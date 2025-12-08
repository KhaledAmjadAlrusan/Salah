package com.knight.salah.di

import com.knight.salah.data.MockSalahApi
import com.knight.salah.data.SalahApi
import com.knight.salah.domain.repoistory.RefreshPrayerUseCase
import com.knight.salah.domain.repoistory.SalahRepository
import com.knight.salah.platformModule
import com.knight.salah.presentation.screens.setting.SettingViewModel
import com.knight.salah.presentation.screens.main.viewmodel.MainPrayerViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }

//    single<SalahApi> { ApiClient(get()) }
    single<SalahApi> { MockSalahApi() }
    single { SalahRepository(get()) }
    single { RefreshPrayerUseCase(get(),get()) }
}

val viewModelModule = module {
    factoryOf(::MainPrayerViewModel)
    factoryOf(::SettingViewModel)
}

fun initKoin() {
    startKoin {
        modules(
            platformModule(),
            dataModule,
            viewModelModule,
        )
    }
}
