package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.drivenext.R
import com.example.drivenext.database.AppDatabase
import com.example.drivenext.database.FavoriteCarEntity
import com.example.drivenext.databinding.ActivityCarDetailsBinding
import com.example.drivenext.model.CarItem
import com.example.drivenext.utils.CarRepository
import kotlinx.coroutines.launch

class CarDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarDetailsBinding
    private var currentCar: CarItem? = null
    private var isFavorite: Boolean = false

    companion object {
        const val EXTRA_CAR = "extra_car"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        @Suppress("DEPRECATION")
        val passedCar = intent.getSerializableExtra(EXTRA_CAR) as? CarItem
        if (passedCar != null) {
            loadCarDetails(passedCar.id, passedCar)
        } else {
            Toast.makeText(this, "Не удалось загрузить данные. Попробуйте снова.", Toast.LENGTH_LONG).show()
            finish()
        }

        binding.btnFavorite.setOnClickListener {
            toggleFavorite()
        }

        binding.btnBook.setOnClickListener {
            currentCar?.let { car ->
                val intent = Intent(this, BookingActivity::class.java).apply {
                    putExtra(BookingActivity.EXTRA_CAR, car)
                }
                startActivity(intent)
            }
        }
    }

    private fun loadCarDetails(carId: String, fallbackCar: CarItem) {
        binding.pbLoading.visibility = View.VISIBLE
        binding.scrollContent.visibility = View.GONE
        binding.bottomBar.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val result = CarRepository.getCarById(carId)
                val car = result.getOrDefault(fallbackCar)
                currentCar = car

                val db = AppDatabase.getInstance(this@CarDetailsActivity)
                isFavorite = db.favoriteDao().isFavorite(car.id)
                val isBooked = db.bookingDao().hasActiveBooking(car.id)
                binding.btnBook.isEnabled = !isBooked
                binding.btnBook.text = if (isBooked) "Забронирован" else "Забронировать"

                binding.ivCarImage.setImageResource(car.imageResId)
                binding.tvCarTitle.text = "${car.brand} ${car.model} ${car.year}"
                binding.tvAddress.text = car.address
                binding.tvDescription.text = car.description
                binding.tvPricePerDay.text = "${car.pricePerDay}Р/день"
                updateFavoriteIcon()

                binding.pbLoading.visibility = View.GONE
                binding.scrollContent.visibility = View.VISIBLE
                binding.bottomBar.visibility = View.VISIBLE
            } catch (e: Exception) {
                binding.pbLoading.visibility = View.GONE
                Toast.makeText(
                    this@CarDetailsActivity,
                    "Не удалось загрузить данные. Попробуйте снова.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun toggleFavorite() {
        val car = currentCar ?: return
        lifecycleScope.launch {
            try {
                val dao = AppDatabase.getInstance(this@CarDetailsActivity).favoriteDao()
                if (isFavorite) {
                    dao.removeFromFavorites(car.id)
                    isFavorite = false
                    Toast.makeText(this@CarDetailsActivity, "Удалено из избранного", Toast.LENGTH_SHORT).show()
                } else {
                    dao.addToFavorites(FavoriteCarEntity.fromCarItem(car))
                    isFavorite = true
                    Toast.makeText(this@CarDetailsActivity, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
                }
                updateFavoriteIcon()
            } catch (e: Exception) {
                Toast.makeText(
                    this@CarDetailsActivity,
                    "Не удалось добавить в избранное. Попробуйте снова.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateFavoriteIcon() {
        val iconRes = if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
        binding.btnFavorite.setImageResource(iconRes)
    }
}