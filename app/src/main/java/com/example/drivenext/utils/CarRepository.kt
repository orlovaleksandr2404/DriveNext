package com.example.drivenext.utils

import com.example.drivenext.R
import com.example.drivenext.model.CarItem
import kotlinx.coroutines.delay

object CarRepository {

    private val carsList = listOf(
        CarItem("1", "Mercedes-Benz", "S 500 Sedan", 2500, "А/Т", "Бензин", R.drawable.ic_car_placeholder),
        CarItem("2", "BMW", "3 Series", 2200, "А/Т", "Дизель", R.drawable.ic_car_placeholder),
        CarItem("3", "Audi", "A4", 2100, "А/Т", "Бензин", R.drawable.ic_car_placeholder),
        CarItem("4", "Tesla", "Model 3", 3000, "А/Т", "Электро", R.drawable.ic_car_placeholder),
        CarItem("5", "Porsche", "Panamera", 4500, "А/Т", "Бензин", R.drawable.ic_car_placeholder)
    )

    suspend fun getAvailableCars(): Result<List<CarItem>> {
        delay(700)
        return Result.success(carsList)
    }

    suspend fun searchCars(query: String): Result<List<CarItem>> {
        delay(700)
        val filtered = carsList.filter {
            it.brand.contains(query, ignoreCase = true) ||
                    it.model.contains(query, ignoreCase = true)
        }
        return Result.success(filtered)
    }
}