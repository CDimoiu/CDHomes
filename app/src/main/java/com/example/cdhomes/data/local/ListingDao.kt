package com.example.cdhomes.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {
  @Query("SELECT * FROM listings")
  fun getAllListings(): Flow<List<ListingEntity>>

  @Query("SELECT * FROM listings WHERE id = :id")
  fun getListingById(id: Int): Flow<ListingEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllListings(listings: List<ListingEntity>)

  @Query("DELETE FROM listings WHERE id = :id")
  suspend fun deleteListingById(id: Int)
}
