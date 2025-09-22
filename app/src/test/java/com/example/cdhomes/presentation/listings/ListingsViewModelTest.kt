package com.example.cdhomes.presentation.listings

import app.cash.turbine.test
import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.model.ListingFilter
import com.example.cdhomes.domain.usecase.DeleteListingUseCase
import com.example.cdhomes.domain.usecase.GetListingsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListingsViewModelTest {

  private val getListingsUseCase: GetListingsUseCase = mockk()
  private val deleteListingUseCase: DeleteListingUseCase = mockk()
  private lateinit var viewModel: ListingsViewModel

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
    ),
    Listing(
      id = 2,
      city = "CityB",
      price = 200_000.0,
      area = 120.0,
      propertyType = "House",
      offerType = 1,
      professional = "Agent",
      bedrooms = 3,
      rooms = 4,
      url = null
    )
  )

  @Before
  fun setup() {
    coEvery { getListingsUseCase() } returns flowOf(sampleListings)
    viewModel = ListingsViewModel(getListingsUseCase, deleteListingUseCase)
  }

  @Test
  fun `uiState emits Loading then Success on init`() = runTest {
    coEvery { getListingsUseCase() } returns flow {
      delay(100)
      emit(sampleListings)
    }
    val viewModel = ListingsViewModel(getListingsUseCase, deleteListingUseCase)

    viewModel.uiState.test {
      assertIs<ListingsUiState.Loading>(awaitItem())
      val success = awaitItem()
      assertIs<ListingsUiState.Success>(success)
      assertEquals(sampleListings, success.listings)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `uiState emits Error when useCase throws exception`() = runTest {
    val errorMessage = "Network error"
    coEvery { getListingsUseCase() } returns flow { throw Exception(errorMessage) }

    viewModel = ListingsViewModel(getListingsUseCase, deleteListingUseCase)

    viewModel.uiState.test {
      val error = awaitItem()
      assertIs<ListingsUiState.Error>(error)
      assertEquals(errorMessage, error.message)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `refreshListings emits Loading then Success`() = runTest {
    coEvery { getListingsUseCase.refreshRemote() } returns Unit
    viewModel.refreshListings()

    viewModel.uiState.test {
      val loading = awaitItem()
      assertIs<ListingsUiState.Loading>(loading)

      val success = awaitItem()
      assertIs<ListingsUiState.Success>(success)
      assertEquals(sampleListings, success.listings)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `refreshListings emits Error on refresh failure`() = runTest {
    val errorMessage = "Refresh failed"
    coEvery { getListingsUseCase.refreshRemote() } throws Exception(errorMessage)

    viewModel.refreshListings()

    viewModel.uiState.test {
      val loading = awaitItem()
      assertIs<ListingsUiState.Loading>(loading)

      val error = awaitItem()
      assertIs<ListingsUiState.Error>(error)
      assertEquals(errorMessage, error.message)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `deleteListing calls deleteUseCase and reloads listings`() = runTest {
    val listingsFlow = MutableStateFlow(sampleListings.toList())

    coEvery { getListingsUseCase() } returns listingsFlow
    coEvery { deleteListingUseCase(1) } coAnswers {
      val updated = listingsFlow.value.filter { it.id != 1 }
      listingsFlow.value = updated
    }

    val viewModel = ListingsViewModel(getListingsUseCase, deleteListingUseCase)

    viewModel.uiState.test {
      val initial = awaitItem()
      assertIs<ListingsUiState.Success>(initial)

      viewModel.deleteListing(1)

      val success = awaitItem()
      assertIs<ListingsUiState.Success>(success)
      assertEquals(listingsFlow.value, success.listings)

      coVerify(exactly = 1) { deleteListingUseCase(1) }
      cancelAndIgnoreRemainingEvents()
    }
  }
}
