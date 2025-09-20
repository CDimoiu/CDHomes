package com.example.cdhomes.presentation.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.usecase.GetListingsUseCase
import com.example.cdhomes.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ListingsViewModel @Inject constructor(
  getListingsUseCase: GetListingsUseCase,
) : ViewModel() {

  val uiState: StateFlow<UiState<List<Listing>>> =
    getListingsUseCase()
      .map { UiState.Success(it) as UiState<List<Listing>> }
      .catch {
        emit(
          UiState.Error(
            it.message ?: "Oops, something went wrong. Please try again later."
          )
        )
      }
      .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)
}
