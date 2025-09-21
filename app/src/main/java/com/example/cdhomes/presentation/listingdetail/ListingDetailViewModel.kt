package com.example.cdhomes.presentation.listingdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.usecase.GetListingDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ListingDetailViewModel @Inject constructor(
  private val getListingDetailsUseCase: GetListingDetailsUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  val uiState: StateFlow<ListingDetailUiState<Listing>> = savedStateHandle.get<String>("id")?.toIntOrNull()?.let { id ->
    getListingDetailsUseCase(id)
      .filterNotNull()
      .map { ListingDetailUiState.Success(it) as ListingDetailUiState<Listing> }
      .catch {
        emit(
          ListingDetailUiState.Error(
            it.message ?: "Oops, something went wrong. Please try again later."
          )
        )
      }
      .stateIn(viewModelScope, SharingStarted.Lazily, ListingDetailUiState.Loading)
  } ?: MutableStateFlow(ListingDetailUiState.Error("Invalid ID"))
}
