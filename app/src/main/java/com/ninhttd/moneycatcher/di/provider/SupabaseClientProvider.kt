package com.ninhttd.moneycatcher.di.provider

import com.ninhttd.moneycatcher.SUPABASE_KEY
import com.ninhttd.moneycatcher.SUPABASE_URL
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseClientProvider {
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Postgrest)
            install(Auth) {
                alwaysAutoRefresh = true
                autoLoadFromStorage = true
            }
        }
    }
}