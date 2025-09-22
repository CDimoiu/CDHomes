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

class GetListingDetailsUseCaseTest {

  private val repository: ListingRepository = mockk()
  private lateinit var useCase: GetListingDetailsUseCase

  private val sampleListing = Listing(
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

  @Before
  fun setup() {
    useCase = GetListingDetailsUseCase(repository)
  }

  @Test
  fun `invoke returns listing from repository`() = runTest {
    coEvery { repository.getListing(1) } returns flowOf(sampleListing)
    val result = useCase(1).first()

    assertEquals(sampleListing, result)
    coVerify(exactly = 1) { repository.getListing(1) }
  }

  @Test
  fun `invoke returns null if repository returns null`() = runTest {
    coEvery { repository.getListing(2) } returns flowOf(null)
    val result = useCase(2).first()

    assertEquals(null, result)
    coVerify(exactly = 1) { repository.getListing(2) }
  }
}
