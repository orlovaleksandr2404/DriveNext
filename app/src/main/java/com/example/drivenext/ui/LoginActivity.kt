package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.MainActivity
import com.example.drivenext.databinding.ActivityLoginBinding
import com.example.drivenext.utils.SessionManager
import java.util.UUID

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|ru)$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInputWatchers()

        binding.btnSubmitLogin.setOnClickListener {
            handleLogin()
        }

        binding.btnGoogleLogin.setOnClickListener {
            handleGoogleLogin()
        }

        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterStep1Activity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Функция восстановления пароля в разработке", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupInputWatchers() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val emailText = binding.etEmail.text?.toString()?.trim().orEmpty()
                val passwordText = binding.etPassword.text?.toString()?.trim().orEmpty()

                binding.btnSubmitLogin.isEnabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
                binding.tilEmail.error = null
                binding.tilPassword.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etEmail.addTextChangedListener(watcher)
        binding.etPassword.addTextChangedListener(watcher)
    }

    private fun handleLogin() {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString()?.trim().orEmpty()

        if (!emailPattern.matches(email)) {
            binding.tilEmail.error = "Введите корректный адрес электронной почты."
            return
        }

        if (password.length < 6) {
            binding.tilPassword.error = "Пароль должен содержать не менее 6 символов"
            return
        }

        val dummyToken = "token_" + UUID.randomUUID().toString()
        SessionManager.saveAuthSession(this, email, dummyToken)

        Toast.makeText(this, "Успешный вход!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun handleGoogleLogin() {
        val googleEmail = "google_user@gmail.com"
        val dummyToken = "google_token_" + UUID.randomUUID().toString()
        SessionManager.saveAuthSession(this, googleEmail, dummyToken)

        Toast.makeText(this, "Вход через Google успешен", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}