package com.example.drivenext.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val bookingNumber: String,
    val carId: String,
    val brand: String,
    val model: String,
    val pricePerDay: Int,
    val insurancePerDay: Int,
    val daysCount: Int,
    val totalPrice: Int,
    val deposit: Int,
    val address: String,
    val startDateText: String,
    val endDateText: String,
    val completedDateText: String,
    val driverName: String,
    val driverLicense: String,
    val status: String, // "Одобрено" или "Завершено"
    val isActive: Boolean,
    val imageResId: Int
) : Serializable