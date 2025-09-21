package com.example.cdhomes.domain.usecase

import com.example.cdhomes.domain.repository.ListingRepository
import javax.inject.Inject

class DeleteListingUseCase @Inject constructor(private val repository: ListingRepository) {
  suspend operator fun invoke(id: Int) {
    repository.deleteListing(id)
  }
}
