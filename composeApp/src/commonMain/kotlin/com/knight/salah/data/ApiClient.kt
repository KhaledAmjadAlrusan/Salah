package com.knight.salah.data

import com.knight.salah.data.prayer.PrayerApi
import com.knight.salah.domain.model.remote.pryaer.DailyPrayerTime
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.utils.io.CancellationException

// Todo: Replace with real api
class ApiClient(
    private val client: HttpClient,
) : PrayerApi {

    companion object {
        private const val BASE_URL =
            "https://kjbutgbpddsadvnbgblg.supabase.co/rest/v1"

        private const val API_KEY =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtqYnV0Z2JwZGRzYWR2bmJnYmxnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mzc3NjQ1NjMsImV4cCI6MjA1MzM0MDU2M30.giaKfNM-hUj2UCrC_ZBUjamv9vFkhP7TORF5xkzyL4Y"

        private const val AUTH_KEY =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtqYnV0Z2JwZGRzYWR2bmJnYmxnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mzc3NjQ1NjMsImV4cCI6MjA1MzM0MDU2M30.giaKfNM-hUj2UCrC_ZBUjamv9vFkhP7TORF5xkzyL4Y"
    }

    override suspend fun getPrayerTime(
        organizationId: String,
        prayerDate: String
    ): DailyPrayerTime? {
        return try {
            client.get("$BASE_URL/daily_prayer_times") {
                header("apikey", API_KEY)
                header("authorization", "Bearer $AUTH_KEY")

                parameter("organization_id", "eq.$organizationId")
                parameter("prayer_date", "eq.$prayerDate")
            }.body<List<DailyPrayerTime>>().firstOrNull()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            null
        }
    }
}
