package com.example.drivenext.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY isActive DESC, id DESC")
    suspend fun getAllBookings(): List<BookingEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM bookings WHERE carId = :carId AND isActive = 1)")
    suspend fun hasActiveBooking(carId: String): Boolean

    @Insert
    suspend fun insertBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET isActive = 0, status = 'Отменено', completedDateText = :completedDate WHERE id = :bookingId")
    suspend fun cancelBooking(bookingId: Long, completedDate: String)

    @Query("SELECT COUNT(*) FROM bookings")
    suspend fun getBookingsCount(): Int
}