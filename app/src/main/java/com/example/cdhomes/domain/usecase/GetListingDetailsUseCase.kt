package com.example.cdhomes.domain.usecase

import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.repository.ListingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetListingDetailsUseCase @Inject constructor(private val repository: ListingRepository) {
  operator fun invoke(id: Int): Flow<Listing?> = repository.getListing(id)
}
