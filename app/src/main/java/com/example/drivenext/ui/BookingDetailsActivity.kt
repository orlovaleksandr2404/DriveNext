package com.example.drivenext.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.drivenext.database.AppDatabase
import com.example.drivenext.database.BookingEntity
import com.example.drivenext.databinding.ActivityBookingDetailsBinding
import kotlinx.coroutines.launch

class BookingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingDetailsBinding
    private lateinit var booking: BookingEntity

    companion object {
        const val EXTRA_BOOKING = "extra_booking"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        val passedBooking = intent.getSerializableExtra(EXTRA_BOOKING) as? BookingEntity
        if (passedBooking == null) {
            finish()
            return
        }
        booking = passedBooking

        binding.btnBack.setOnClickListener { finish() }

        bindData()

        binding.btnCancelBooking.setOnClickListener {
            cancelCurrentBooking()
        }
    }

    private fun bindData() {
        binding.tvHeader.text = "Бронирование # ${booking.bookingNumber}"
        binding.tvCarModel.text = booking.model
        binding.tvCarBrand.text = booking.brand
        binding.tvCarPrice.text = "${booking.pricePerDay}Р в день"
        binding.ivCarImage.setImageResource(booking.imageResId)

        binding.tvAddress.text = booking.address
        binding.tvStartDate.text = booking.startDateText
        binding.tvEndDate.text = booking.endDateText
        binding.tvDriverName.text = booking.driverName
        binding.tvDriverLicense.text = booking.driverLicense
        binding.tvStatus.text = booking.status

        val rentTotal = booking.pricePerDay * booking.daysCount
        val insuranceTotal = booking.insurancePerDay * booking.daysCount

        binding.tvRentLabel.text = "Аренда х${booking.daysCount} дня:"
        binding.tvRentValue.text = "${rentTotal}Р (${booking.pricePerDay}Р/день)"

        binding.tvInsuranceLabel.text = "Страховка х${booking.daysCount} дня:"
        binding.tvInsuranceValue.text = "${insuranceTotal}Р (${booking.insurancePerDay}Р/день)"

        binding.tvTotalPrice.text = "${booking.totalPrice}Р"

        binding.btnCancelBooking.visibility = if (booking.isActive) View.VISIBLE else View.GONE
    }

    private fun cancelCurrentBooking() {
        lifecycleScope.launch {
            AppDatabase.getInstance(this@BookingDetailsActivity)
                .bookingDao()
                .cancelBooking(booking.id, booking.completedDateText)

            Toast.makeText(this@BookingDetailsActivity, "Бронирование отменено", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}