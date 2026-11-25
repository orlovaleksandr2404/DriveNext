package com.example.drivenext.utils

import com.example.drivenext.R
import com.example.drivenext.model.CarItem
import kotlinx.coroutines.delay

object CarRepository {

    private val carsList = listOf(
        CarItem(
            id = "1",
            brand = "Mercedes-Benz",
            model = "S 500 Sedan",
            pricePerDay = 2500,
            transmission = "A/T",
            fuelType = "Бензин",
            imageResId = R.drawable.car_mercedes,
            address = "Авиамоторная ул., 8, стр. 2",
            year = 2023,
            description = "Mercedes-Benz S 500 оснащен 3,0-литровым рядным 6-цилиндровым бензиновым двигателем мощностью 435 л.с., полным приводом 4MATIC и 9-ступенчатой автоматической коробкой передач."
        ),
        CarItem(
            id = "2",
            brand = "BMW",
            model = "3 Series",
            pricePerDay = 2200,
            transmission = "A/T",
            fuelType = "Дизель",
            imageResId = R.drawable.car_bmw,
            address = "Авиамоторная ул., 8, стр. 2",
            year = 2021,
            description = "BMW 3 Series оснащена 2,0-литровым дизельным двигателем TwinPower Turbo мощностью 190 л.с., задним приводом и 8-ступенчатой спортивной автоматической трансмиссией."
        ),
        CarItem(
            id = "3",
            brand = "Audi",
            model = "A4",
            pricePerDay = 2100,
            transmission = "A/T",
            fuelType = "Бензин",
            imageResId = R.drawable.car_audi,
            address = "Авиамоторная ул., 8, стр. 2",
            year = 2022,
            description = "Audi A4 оснащена 2,0-литровым двигателем TFSI мощностью 249 л.с., фирменным полным приводом quattro и роботизированной коробкой передач S tronic."
        ),
        CarItem(
            id = "4",
            brand = "Tesla",
            model = "Model 3",
            pricePerDay = 3000,
            transmission = "A/T",
            fuelType = "Электро",
            imageResId = R.drawable.car_tesla,
            address = "Авиамоторная ул., 8, стр. 2",
            year = 2019,
            description = "Tesla Model 3 оснащена высокоэффективным электродвигателем, обеспечивающим мгновенный крутящий момент, запас хода до 500 км и автопилот."
        ),
        CarItem(
            id = "5",
            brand = "Porsche",
            model = "Panamera",
            pricePerDay = 4500,
            transmission = "A/T",
            fuelType = "Бензин",
            imageResId = R.drawable.car_porsche,
            address = "Авиамоторная ул., 8, стр. 2",
            year = 2023,
            description = "Porsche Panamera оснащена 2,9-литровым битурбированным двигателем V6 мощностью 330 л.с., полным приводом и 8-ступенчатой коробкой PDK."
        )
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

    suspend fun getCarById(carId: String): Result<CarItem> {
        delay(400)
        val car = carsList.find { it.id == carId }
        return if (car != null) {
            Result.success(car)
        } else {
            Result.failure(Exception("Автомобиль не найден"))
        }
    }
}