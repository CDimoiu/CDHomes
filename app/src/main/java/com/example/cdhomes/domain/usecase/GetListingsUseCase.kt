package com.example.cdhomes.domain.usecase

import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.repository.ListingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetListingsUseCase @Inject constructor(private val repository: ListingRepository) {
  operator fun invoke(): Flow<List<Listing>> = repository.getListings()
}
