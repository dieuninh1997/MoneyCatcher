package com.ninhttd.moneycatcher.data.repository

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.ninhttd.moneycatcher.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<UserInfo?> {
        return try {
            val res = loginOrSignUp(email, password)
            Result.success(res)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private suspend fun loginOrSignUp(email: String, password: String): UserInfo? {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            auth.currentUserOrNull()

        } catch (e: Exception) {
            Timber.Forest.tag("AUTH").e("Login failed: ${e.message}")

            if ("User not found" in e.message.orEmpty() || "Invalid login credentials" in e.message.orEmpty()) {
                try {
                    auth.signUpWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    Timber.Forest.tag("AUTH").e("Đăng ký thành công")

                    auth.signInWith(Email) {
                        this.email = email
                        this.password = password
                    }

                    Timber.Forest.tag("AUTH").e("Đăng nhập lại sau đăng ký thành công")
                    auth.currentUserOrNull()
                } catch (signupError: Exception) {
                    Timber.Forest.tag("AUTH").e("Đăng ký thất bại: ${signupError.message}")
                    null
                }
            } else {
                Timber.Forest.tag("AUTH").e("Sai mật khẩu hoặc tài khoản")
                null
            }
        }
    }

    private fun loginWithGoogle(context: Context) {
        val redirectUrl = "moneycatcher://login-callback"
        val loginUrl =
            "https://zplpljhlxxzyxgfabwtl.supabase.co/auth/v1/authorize?provider=google&redirect_to=$redirectUrl"

        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(context, loginUrl.toUri())
    }

}