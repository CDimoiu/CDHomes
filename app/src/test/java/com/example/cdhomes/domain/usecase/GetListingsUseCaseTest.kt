package com.example.cdhomes.domain.usecase

import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.repository.ListingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetListingsUseCaseTest {

  private val repository: ListingRepository = mockk()
  private lateinit var useCase: GetListingsUseCase

  private val sampleListings = listOf(
    Listing(
      id = 1,
      city = "CityA",
      price = 100_000.0,
      area = 80.0,
      propertyType = "Apartment",
      offerType = 1,
      professional = "Agent",
      bedrooms = 2,
      rooms = 3,
      url = null
    )
  )

  @Before
  fun setup() {
    useCase = GetListingsUseCase(repository)
  }

  @Test
  fun `invoke returns listings from repository`() = runTest {
    coEvery { repository.getListings() } returns flowOf(sampleListings)
    val result = useCase().first()

    assertEquals(sampleListings, result)
    coVerify(exactly = 1) { repository.getListings() }
  }

  @Test
  fun `refreshRemote calls repository refreshListings`() = runTest {
    coEvery { repository.refreshListings() } returns Unit
    useCase.refreshRemote()

    coVerify(exactly = 1) { repository.refreshListings() }
  }
}
