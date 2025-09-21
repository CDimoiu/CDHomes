package com.example.cdhomes.domain.usecase

import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.model.ListingFilter
import com.example.cdhomes.domain.repository.ListingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetListingsUseCase @Inject constructor(private val repository: ListingRepository) {

  operator fun invoke(filter: ListingFilter = ListingFilter()): Flow<List<Listing>> {
    return repository.getListings().map { listings ->
      listings.filter { listing ->
        (filter.minPrice?.let { listing.price >= it } ?: true) &&
          (filter.maxPrice?.let { listing.price <= it } ?: true) &&
          (filter.minRooms?.let { listing.rooms?.let { rooms -> rooms >= it } ?: false } ?: true) &&
          (filter.maxRooms?.let { listing.rooms?.let { rooms -> rooms <= it } ?: false } ?: true) &&
          (filter.minArea?.let { listing.area >= it } ?: true) &&
          (filter.maxArea?.let { listing.area <= it } ?: true) &&
          (filter.city?.let { listing.city.contains(it, ignoreCase = true) } ?: true)
      }
    }
  }

  suspend fun refreshRemote() {
    repository.refreshListings()
  }
}
