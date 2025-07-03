package com.ninhttd.moneycatcher

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import timber.log.Timber

const val SUPABASE_URL = "https://zplpljhlxxzyxgfabwtl.supabase.co"
const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpwbHBsamhseHh6eXhnZmFid3RsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTE0NDA4NDIsImV4cCI6MjA2NzAxNjg0Mn0.R6ku87Iws4funZ7p5NrPUOIanBoAWjwvDuj3NG5XXPs"

@HiltAndroidApp
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber DebugTree planted")
        }


    }
}