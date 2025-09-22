package com.example.cdhomes.presentation.listingdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.usecase.GetListingDetailsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListingDetailViewModelTest {

  private val useCase: GetListingDetailsUseCase = mockk()
  private lateinit var savedStateHandle: SavedStateHandle

  private lateinit var viewModel: ListingDetailViewModel

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

  @BeforeTest
  fun setup() {
    savedStateHandle = SavedStateHandle(mapOf("id" to "1"))
  }

  @Test
  fun `uiState emits Success when useCase returns listing`() = runTest {
    coEvery { useCase(1) } returns kotlinx.coroutines.flow.flowOf(sampleListing)
    viewModel = ListingDetailViewModel(useCase, savedStateHandle)

    viewModel.uiState.test {
      assertIs<ListingDetailUiState.Loading>(awaitItem())
      val emission = awaitItem()
      assertIs<ListingDetailUiState.Success<Listing>>(emission)
      assertEquals(sampleListing, emission.data)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `uiState emits Error when useCase throws exception`() = runTest {
    val errorMessage = "Network error"
    coEvery { useCase(1) } returns kotlinx.coroutines.flow.flow {
      throw Exception(errorMessage)
    }
    viewModel = ListingDetailViewModel(useCase, savedStateHandle)

    viewModel.uiState.test {
      assertIs<ListingDetailUiState.Loading>(awaitItem())
      val emission = awaitItem()
      assertIs<ListingDetailUiState.Error>(emission)
      assertEquals(errorMessage, emission.message)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `uiState emits Error when id is invalid`() = runTest {
    savedStateHandle = SavedStateHandle(mapOf("id" to "invalid"))
    viewModel = ListingDetailViewModel(useCase, savedStateHandle)

    viewModel.uiState.test {
      val emission = awaitItem()
      assertIs<ListingDetailUiState.Error>(emission)
      assertEquals(null, emission.message)
      cancelAndIgnoreRemainingEvents()
    }
  }
}
