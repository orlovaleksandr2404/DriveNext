package com.example.drivenext.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drivenext.databinding.ItemCarBinding
import com.example.drivenext.model.CarItem

class CarAdapter(
    private var cars: List<CarItem>,
    private val onBookClick: (CarItem) -> Unit,
    private val onDetailsClick: (CarItem) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(val binding: ItemCarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]
        with(holder.binding) {
            tvCarModel.text = car.model
            tvCarBrand.text = car.brand
            tvCarPrice.text = "${car.pricePerDay}₽ / день"
            tvTransmission.text = "⚙ ${car.transmission}"
            tvFuel.text = "⛽ ${car.fuelType}"
            ivCarImage.setImageResource(car.imageResId)

            btnBook.setOnClickListener { onBookClick(car) }
            btnDetails.setOnClickListener { onDetailsClick(car) }
        }
    }

    override fun getItemCount(): Int = cars.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCars: List<CarItem>) {
        cars = newCars
        notifyDataSetChanged()
    }
}