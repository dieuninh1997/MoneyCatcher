package com.ninhttd.moneycatcher.di

import io.github.jan.supabase.auth.user.UserInfo

object SessionManager {
    var currentUser: UserInfo? = null
}