package com.example.drivenext.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.drivenext.model.CarItem

@Entity(tableName = "favorite_cars")
data class FavoriteCarEntity(
    @PrimaryKey val carId: String,
    val brand: String,
    val model: String,
    val pricePerDay: Int,
    val transmission: String,
    val fuelType: String,
    val imageResId: Int,
    val address: String,
    val year: Int,
    val description: String
) {
    fun toCarItem(): CarItem = CarItem(
        id = carId,
        brand = brand,
        model = model,
        pricePerDay = pricePerDay,
        transmission = transmission,
        fuelType = fuelType,
        imageResId = imageResId,
        address = address,
        year = year,
        description = description
    )

    companion object {
        fun fromCarItem(car: CarItem): FavoriteCarEntity = FavoriteCarEntity(
            carId = car.id,
            brand = car.brand,
            model = car.model,
            pricePerDay = car.pricePerDay,
            transmission = car.transmission,
            fuelType = car.fuelType,
            imageResId = car.imageResId,
            address = car.address,
            year = car.year,
            description = car.description
        )
    }
}