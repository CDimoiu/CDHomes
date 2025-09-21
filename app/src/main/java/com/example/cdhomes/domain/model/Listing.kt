package com.example.cdhomes.domain.model

data class Listing(
  val id: Int,
  val city: String,
  val price: Double,
  val area: Double,
  val propertyType: String,
  val offerType: Int,
  val professional: String,
  val bedrooms: Int?,
  val rooms: Int?,
  val url: String?,
)
