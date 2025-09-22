package com.example.cdhomes.domain.usecase

import com.example.cdhomes.domain.repository.ListingRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteListingUseCaseTest {

  private val repository: ListingRepository = mockk(relaxed = true)
  private lateinit var useCase: DeleteListingUseCase

  @Before
  fun setup() {
    useCase = DeleteListingUseCase(repository)
  }

  @Test
  fun `invoke calls repository deleteListing`() = runTest {
    useCase(1)

    coVerify(exactly = 1) { repository.deleteListing(1) }
  }

  @Test
  fun `invoke calls repository with different id`() = runTest {
    useCase(42)

    coVerify(exactly = 1) { repository.deleteListing(42) }
  }
}
