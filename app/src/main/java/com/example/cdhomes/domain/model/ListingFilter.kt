package com.example.cdhomes.domain.model

data class ListingFilter(
  val minPrice: Double? = null,
  val maxPrice: Double? = null,
  val minRooms: Int? = null,
  val maxRooms: Int? = null,
  val minArea: Double? = null,
  val maxArea: Double? = null,
  val city: String? = null
)
