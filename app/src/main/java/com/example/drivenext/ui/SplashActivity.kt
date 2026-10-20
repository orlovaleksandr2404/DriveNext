package com.example.drivenext.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.MainActivity
import com.example.drivenext.R
import com.example.drivenext.utils.NetworkHelper
import com.example.drivenext.utils.SessionManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkNetworkAndNavigate()
        }, 2500)
    }

    private fun checkNetworkAndNavigate() {
        if (!NetworkHelper.hasInternetConnection(this)) {
            startActivity(Intent(this, NoInternetActivity::class.java))
            finish()
            return
        }

        if (SessionManager.isLoggedIn(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isOnboardingCompleted = prefs.getBoolean("is_onboarding_completed", false)

        if (!isOnboardingCompleted) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        } else {
            startActivity(Intent(this, AuthChoiceActivity::class.java))
        }
        finish()
    }
}