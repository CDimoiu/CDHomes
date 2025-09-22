package com.example.cdhomes.data.remote

import com.google.gson.annotations.SerializedName

data class ListingsResponse(
  @SerializedName("items") val items: List<ListingDto>,
  @SerializedName("totalCount") val totalCount: Int,
)
