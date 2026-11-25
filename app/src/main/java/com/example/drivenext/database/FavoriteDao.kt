package com.example.drivenext.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_cars")
    suspend fun getAllFavorites(): List<FavoriteCarEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cars WHERE carId = :carId)")
    suspend fun isFavorite(carId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(car: FavoriteCarEntity)

    @Query("DELETE FROM favorite_cars WHERE carId = :carId")
    suspend fun removeFromFavorites(carId: String)
}