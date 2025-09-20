package com.example.cdhomes.domain.repository

import com.example.cdhomes.domain.model.Listing
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
  fun getListings(): Flow<List<Listing>>
  fun getListing(id: Int): Flow<Listing?>
}