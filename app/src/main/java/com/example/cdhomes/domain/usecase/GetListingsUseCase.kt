package com.example.cdhomes.domain.usecase

import com.example.cdhomes.domain.repository.ListingRepository
import javax.inject.Inject

class GetListingsUseCase @Inject constructor(
  private val repository: ListingRepository,
) {
  operator fun invoke() = repository.getListings()
}
git