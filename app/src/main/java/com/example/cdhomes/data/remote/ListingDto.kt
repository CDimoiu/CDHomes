package com.example.cdhomes.data.remote

import com.google.gson.annotations.SerializedName

data class ListingDto(
  @SerializedName("id") val id: Int,
  @SerializedName("city") val city: String,
  @SerializedName("price") val price: Double,
  @SerializedName("area") val area: Double,
  @SerializedName("propertyType") val propertyType: String,
  @SerializedName("offerType") val offerType: Int,
  @SerializedName("professional") val professional: String,
  @SerializedName("bedrooms") val bedrooms: Int? = null,
  @SerializedName("rooms") val rooms: Int? = null,
  @SerializedName("url") val url: String? = null,
)
