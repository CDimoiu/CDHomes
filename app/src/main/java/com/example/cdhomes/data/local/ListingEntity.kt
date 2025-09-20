package com.example.cdhomes.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listings")
data class ListingEntity(
  @PrimaryKey val id: Int,
  val city: String,
  val price: Double,
  val area: Double,
  val propertyType: String,
  val offerType: Int,
  val professional: String,
  val bedrooms: Int?,
  val rooms: Int?,
  val url: String?
)
