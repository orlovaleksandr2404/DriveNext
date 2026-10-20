package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.R
import com.example.drivenext.utils.NetworkHelper

class NoInternetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)

        val btnRetry = findViewById<Button>(R.id.btnRetry)
        btnRetry.setOnClickListener {
            if (NetworkHelper.hasInternetConnection(this)) {
                val intent = Intent(this, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Интернет по-прежнему недоступен", Toast.LENGTH_SHORT).show()
            }
        }
    }
}