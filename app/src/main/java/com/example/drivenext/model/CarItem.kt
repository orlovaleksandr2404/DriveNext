package com.example.drivenext.model

import java.io.Serializable

data class CarItem(
    val id: String,
    val brand: String,
    val model: String,
    val pricePerDay: Int,
    val transmission: String,
    val fuelType: String,
    val imageResId: Int,
    val address: String = "Авиамоторная ул., 8, стр. 2",
    val year: Int = 2022,
    val description: String = "Автомобиль оснащен современным турбированным двигателем, автоматической коробкой передач, климат-контролем и системой помощи водителю."
) : Serializable