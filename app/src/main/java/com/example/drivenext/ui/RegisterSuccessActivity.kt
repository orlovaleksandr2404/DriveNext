package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.MainActivity
import com.example.drivenext.databinding.ActivityRegisterSuccessBinding
import com.example.drivenext.utils.SessionManager
import java.util.UUID

class RegisterSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dummyToken = "reg_token_" + UUID.randomUUID().toString()
        SessionManager.saveAuthSession(this, "registered_user@example.com", dummyToken)

        binding.btnDone.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}