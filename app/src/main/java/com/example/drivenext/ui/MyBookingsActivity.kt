package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivenext.MainActivity
import com.example.drivenext.database.AppDatabase
import com.example.drivenext.databinding.ActivityMyBookingsBinding
import kotlinx.coroutines.launch

class MyBookingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyBookingsBinding
    private lateinit var bookingAdapter: BookingAdapter
    private var fromSuccess: Boolean = false

    companion object {
        const val EXTRA_FROM_SUCCESS = "extra_from_success"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fromSuccess = intent.getBooleanExtra(EXTRA_FROM_SUCCESS, false)

        binding.btnBack.setOnClickListener {
            handleBackNavigation()
        }

        bookingAdapter = BookingAdapter(emptyList()) { activeBooking ->
            val intent = Intent(this, BookingDetailsActivity::class.java).apply {
                putExtra(BookingDetailsActivity.EXTRA_BOOKING, activeBooking)
            }
            startActivity(intent)
        }

        binding.rvBookings.layoutManager = LinearLayoutManager(this)
        binding.rvBookings.adapter = bookingAdapter
    }

    override fun onResume() {
        super.onResume()
        loadBookings()
    }

    private fun loadBookings() {
        lifecycleScope.launch {
            val list = AppDatabase.getInstance(this@MyBookingsActivity)
                .bookingDao()
                .getAllBookings()
            bookingAdapter.updateData(list)
        }
    }

    private fun handleBackNavigation() {
        if (fromSuccess) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        finish()
    }
}