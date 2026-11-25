package com.example.drivenext.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drivenext.database.BookingEntity
import com.example.drivenext.databinding.ItemBookingBinding

class BookingAdapter(
    private var bookings: List<BookingEntity>,
    private val onActiveBookingClick: (BookingEntity) -> Unit
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(val binding: ItemBookingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        with(holder.binding) {
            tvCarName.text = "${booking.brand} ${booking.model}"
            if (booking.isActive) {
                tvBookingSubtitle.text = "Начало аренды: ${booking.startDateText}"
                root.setOnClickListener { onActiveBookingClick(booking) }
            } else {
                tvBookingSubtitle.text = "Аренда завершена, ${booking.completedDateText}"
                root.setOnClickListener(null)
            }
        }
    }

    override fun getItemCount(): Int = bookings.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newBookings: List<BookingEntity>) {
        bookings = newBookings
        notifyDataSetChanged()
    }
}