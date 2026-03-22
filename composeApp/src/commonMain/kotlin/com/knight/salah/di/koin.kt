package com.knight.salah.di

import com.knight.salah.data.datastore.mosue.MosqueDataSource
import com.knight.salah.data.datastore.setting.SettingDataSource
import com.knight.salah.data.mosque.MosqueApi
import com.knight.salah.data.mosque.mock.MockMosqueApi
import com.knight.salah.data.prayer.PrayerApi
import com.knight.salah.data.prayer.mock.MockPrayerApi
import com.knight.salah.domain.repoistory.mosque.MosqueRepository
import com.knight.salah.domain.repoistory.prayer.RefreshPrayerUseCase
import com.knight.salah.domain.repoistory.prayer.SalahRepository
import com.knight.salah.domain.repoistory.setting.SettingRepository
import com.knight.salah.platformModule
import com.knight.salah.presentation.screens.main.viewmodel.MainPrayerViewModel
import com.knight.salah.presentation.screens.search.viewmodel.SearchMosqueViewModel
import com.knight.salah.presentation.screens.setting.viewmodel.SettingViewModel
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

    //DataSource
    single<PrayerApi> { MockPrayerApi() }
    single<MosqueApi> { MockMosqueApi() }
    single { SettingDataSource(get()) }
    single { MosqueDataSource(get()) }

    //Repository
    single { SalahRepository(get()) }
    single { SettingRepository(get()) }
    single { MosqueRepository(get()) }

    //UseCase
    single {
        RefreshPrayerUseCase(
            get(), get(),
            notificationManager = get()
        )
    }
}

val viewModelModule = module {
    factoryOf(::MainPrayerViewModel)
    factoryOf(::SettingViewModel)
    factoryOf(::SearchMosqueViewModel)
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
