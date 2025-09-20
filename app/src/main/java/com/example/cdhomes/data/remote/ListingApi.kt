package com.example.cdhomes.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ListingApi {
  @GET("listings.json")
  suspend fun getListings(): ListingsResponse

  @GET("listings/{id}.json")
  suspend fun getListing(@Path("id") id: Int): ListingDto
}