package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.databinding.ActivityRegisterStep1Binding

class RegisterStep1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterStep1Binding
    private val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|ru)$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnNext.setOnClickListener {
            validateAndProceed()
        }
    }

    private fun validateAndProceed() {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString()?.trim().orEmpty()
        val repeatPassword = binding.etRepeatPassword.text?.toString()?.trim().orEmpty()
        val isAgreed = binding.cbTerms.isChecked

        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilRepeatPassword.error = null

        var hasError = false

        if (!emailPattern.matches(email)) {
            binding.tilEmail.error = "Введите корректный адрес электронной почты."
            hasError = true
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Пожалуйста, заполните пароль"
            hasError = true
        }

        if (password != repeatPassword) {
            binding.tilRepeatPassword.error = "Пароли не совпадают."
            hasError = true
        }

        if (!isAgreed) {
            Toast.makeText(
                this,
                "Необходимо согласиться с условиями обслуживания и политикой конфиденциальности.",
                Toast.LENGTH_LONG
            ).show()
            hasError = true
        }

        if (hasError) return

        val intent = Intent(this, RegisterStep2Activity::class.java).apply {
            putExtra("EXTRA_EMAIL", email)
            putExtra("EXTRA_PASSWORD", password)
        }
        startActivity(intent)
    }
}