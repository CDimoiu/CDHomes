package com.example.cdhomes.data.repository

import com.example.cdhomes.data.local.ListingDao
import com.example.cdhomes.data.local.ListingEntity
import com.example.cdhomes.data.remote.ListingApi
import com.example.cdhomes.data.remote.ListingDto
import com.example.cdhomes.data.remote.ListingsResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListingRepositoryImplTest {

  private val dao: ListingDao = mockk(relaxed = true)
  private val api: ListingApi = mockk()
  private lateinit var repository: ListingRepositoryImpl

  private val sampleListingEntity = ListingEntity(
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
  private val sampleListing = sampleListingEntity.toDomain()

  private val remoteResponse = ListingsResponse(
    items = listOf(
      ListingDto(
        id = 2,
        city = "CityB",
        price = 200_000.0,
        area = 120.0,
        propertyType = "House",
        offerType = 1,
        professional = "Broker",
        bedrooms = 3,
        rooms = 4,
        url = null,
      )
    ),
    totalCount = 1,
  )

  @Before
  fun setup() {
    repository = ListingRepositoryImpl(dao, api)
  }

  @Test
  fun `getListings emits local when available`() = runTest {
    every { dao.getAllListings() } returns flowOf(listOf(sampleListingEntity))

    val result = repository.getListings().first()

    assertEquals(listOf(sampleListing), result)
    coVerify(exactly = 0) { api.getListings() }
  }

  @Test
  fun `getListings fetches remote when local empty`() = runTest {
    every { dao.getAllListings() } returns flowOf(emptyList())
    coEvery { api.getListings() } returns remoteResponse
    coEvery { dao.insertAllListings(any()) } just Runs

    val result = repository.getListings().first()

    assertEquals(remoteResponse.items.map { it.toEntity().toDomain() }, result)
    coVerify { dao.insertAllListings(remoteResponse.items.map { it.toEntity() }) }
  }

  @Test
  fun `getListing returns local item if exists`() = runTest {
    every { dao.getListingById(1) } returns flowOf(sampleListingEntity)

    val result = repository.getListing(1).first()

    assertEquals(sampleListing, result)
    coVerify(exactly = 0) { api.getListings() }
  }

  @Test
  fun `getListing fetches remote when local is null`() = runTest {
    every { dao.getListingById(2) } returns flowOf(null)
    val remoteItem = remoteResponse.items.first()
    coEvery { api.getListing(2) } returns remoteItem
    coEvery { dao.insertAllListings(any()) } just Runs

    val result = repository.getListing(2).first()

    assertEquals(remoteItem.toEntity().toDomain(), result)
    coVerify { dao.insertAllListings(listOf(remoteItem.toEntity())) }
  }

  @Test
  fun `refreshListings calls api and inserts into db`() = runTest {
    coEvery { api.getListings() } returns remoteResponse
    coEvery { dao.insertAllListings(any()) } just Runs

    repository.refreshListings()

    coVerify { dao.insertAllListings(remoteResponse.items.map { it.toEntity() }) }
  }

  @Test
  fun `deleteListing removes item from db`() = runTest {
    coEvery { dao.deleteListingById(1) } just Runs

    repository.deleteListing(1)

    coVerify { dao.deleteListingById(1) }
  }
}
