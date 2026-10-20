package com.example.drivenext.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.drivenext.MainActivity
import com.example.drivenext.R
import com.example.drivenext.model.OnboardingItem

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var tvSkip: TextView

    private val onboardingPages = listOf(
        OnboardingItem("Аренда автомобилей", "Открой для себя удобный и доступный способ передвижения.", R.drawable.ic_onboard_1),
        OnboardingItem("Безопасно и удобно", "Арендуй автомобиль и наслаждайся его удобством", R.drawable.ic_onboard_2),
        OnboardingItem("Лучшие предложения", "Выбирай понравившееся среди сотен доступных автомобилей", R.drawable.ic_onboard_3)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btnNext)
        tvSkip = findViewById(R.id.tvSkip)

        viewPager.adapter = OnboardingAdapter(onboardingPages)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == onboardingPages.size - 1) {
                    btnNext.text = "Поехали"
                } else {
                    btnNext.text = "Далее"
                }
            }
        })

        btnNext.setOnClickListener {
            val current = viewPager.currentItem
            if (current < onboardingPages.size - 1) {
                viewPager.setCurrentItem(current + 1, true)
            } else {
                finishOnboarding()
            }
        }

        tvSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun finishOnboarding() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_onboarding_completed", true).apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}