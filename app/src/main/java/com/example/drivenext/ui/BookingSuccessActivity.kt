package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.MainActivity
import com.example.drivenext.databinding.ActivityBookingSuccessBinding

class BookingSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMyBookings.setOnClickListener {
            val intent = Intent(this, MyBookingsActivity::class.java).apply {
                putExtra(MyBookingsActivity.EXTRA_FROM_SUCCESS, true)
            }
            startActivity(intent)
            finish()
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}