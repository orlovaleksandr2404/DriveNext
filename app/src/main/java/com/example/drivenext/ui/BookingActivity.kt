package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.drivenext.database.AppDatabase
import com.example.drivenext.database.BookingEntity
import com.example.drivenext.databinding.ActivityBookingBinding
import com.example.drivenext.model.CarItem
import com.example.drivenext.utils.SessionManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.max
import kotlin.random.Random

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private lateinit var car: CarItem

    private val startCal: Calendar = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 8)
        set(Calendar.MINUTE, 0)
    }

    private val endCal: Calendar = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, 4)
        set(Calendar.HOUR_OF_DAY, 8)
        set(Calendar.MINUTE, 0)
    }

    private val insurancePerDay = 300
    private val depositAmount = 15000
    private val displayFormatter = SimpleDateFormat("HH:mm, d MMMM yyyy", Locale("ru"))
    private val shortDateFormatter = SimpleDateFormat("d MMMM yyyy", Locale("ru"))

    companion object {
        const val EXTRA_CAR = "extra_car"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        val passedCar = intent.getSerializableExtra(EXTRA_CAR) as? CarItem
        if (passedCar == null) {
            finish()
            return
        }
        car = passedCar

        binding.btnBack.setOnClickListener { finish() }

        bindCarInfo()
        updateDatesAndPrices()
        checkIfAlreadyBooked()

        binding.rowStartDate.setOnClickListener {
            pickDateTime(isStart = true)
        }

        binding.rowEndDate.setOnClickListener {
            pickDateTime(isStart = false)
        }

        binding.btnContinue.setOnClickListener {
            createBookingAndProceed()
        }
    }

    private fun bindCarInfo() {
        binding.tvCarModel.text = car.model
        binding.tvCarBrand.text = car.brand
        binding.tvCarPrice.text = "${car.pricePerDay}Р в день"
        binding.ivCarImage.setImageResource(car.imageResId)
        binding.tvAddress.text = car.address
    }

    private fun pickDateTime(isStart: Boolean) {
        val targetCal = if (isStart) startCal else endCal
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(if (isStart) "Дата начала аренды" else "Дата окончания аренды")
            .setCalendarConstraints(constraints)
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedMillis ->
            val pickedDate = Calendar.getInstance().apply { timeInMillis = selectedMillis }

            val timePicker = MaterialTimePicker.Builder()
                .setTitleText("Выберите время")
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(targetCal.get(Calendar.HOUR_OF_DAY))
                .setMinute(targetCal.get(Calendar.MINUTE))
                .build()

            timePicker.addOnPositiveButtonClickListener {
                targetCal.set(
                    pickedDate.get(Calendar.YEAR),
                    pickedDate.get(Calendar.MONTH),
                    pickedDate.get(Calendar.DAY_OF_MONTH),
                    timePicker.hour,
                    timePicker.minute
                )

                // Если конец аренды раньше или равен началу, сдвигаем на +1 день
                if (endCal.timeInMillis <= startCal.timeInMillis) {
                    endCal.timeInMillis = startCal.timeInMillis + 24L * 60 * 60 * 1000
                }
                updateDatesAndPrices()
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER")
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }

    private fun calculateDays(): Int {
        val diffMillis = endCal.timeInMillis - startCal.timeInMillis
        val days = (diffMillis / (1000 * 60 * 60 * 24)).toInt()
        return max(1, days)
    }

    private fun updateDatesAndPrices() {
        val days = calculateDays()
        val rentTotal = car.pricePerDay * days
        val insuranceTotal = insurancePerDay * days
        val total = rentTotal + insuranceTotal

        binding.tvStartDate.text = displayFormatter.format(startCal.time)
        binding.tvEndDate.text = displayFormatter.format(endCal.time)

        binding.tvRentLabel.text = "Аренда автомобиля х$days дня:"
        binding.tvRentPrice.text = "${rentTotal}Р (${car.pricePerDay}Р/день)"

        binding.tvInsuranceLabel.text = "Страховка х$days дня:"
        binding.tvInsurancePrice.text = "${insuranceTotal}Р (${insurancePerDay}Р/день)"

        binding.tvTotalPrice.text = "${total}Р"
        binding.tvDeposit.text = "${depositAmount}Р"
    }

    private fun createBookingAndProceed() {
        binding.btnContinue.isEnabled = false

        val days = calculateDays()
        val total = (car.pricePerDay + insurancePerDay) * days
        val bookingNum = Random.nextInt(100000, 999999).toString()

        val booking = BookingEntity(
            bookingNumber = bookingNum,
            carId = car.id,
            brand = car.brand,
            model = car.model,
            pricePerDay = car.pricePerDay,
            insurancePerDay = insurancePerDay,
            daysCount = days,
            totalPrice = total,
            deposit = depositAmount,
            address = car.address,
            startDateText = displayFormatter.format(startCal.time),
            endDateText = displayFormatter.format(endCal.time),
            completedDateText = shortDateFormatter.format(endCal.time),
            driverName = SessionManager.getUserName(this),
            driverLicense = SessionManager.getDriverLicense(this),
            status = "Одобрено",
            isActive = true,
            imageResId = car.imageResId
        )

        lifecycleScope.launch {
            try {
                val dao = AppDatabase.getInstance(this@BookingActivity).bookingDao()
                if (dao.hasActiveBooking(car.id)) {
                    Toast.makeText(
                        this@BookingActivity,
                        "Этот автомобиль уже забронирован",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.btnContinue.text = "Автомобиль уже забронирован"
                    return@launch
                }

                dao.insertBooking(booking)

                val intent = Intent(this@BookingActivity, BookingSuccessActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                binding.btnContinue.isEnabled = true
                Toast.makeText(this@BookingActivity, "Ошибка при создании бронирования", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkIfAlreadyBooked() {
        lifecycleScope.launch {
            val isAlreadyBooked = AppDatabase.getInstance(this@BookingActivity)
                .bookingDao()
                .hasActiveBooking(car.id)

            if (isAlreadyBooked) {
                binding.btnContinue.isEnabled = false
                binding.btnContinue.text = "Автомобиль уже забронирован"
            }
        }
    }
}