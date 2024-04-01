package dev.bittim.valolink.feature.content.ui.contracts

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ContractsViewModel @Inject constructor(
    
) : ViewModel() {
    private var _contractsState = MutableStateFlow<ContractsState>(ContractsState.Fetching)
    val contractsState = _contractsState.asStateFlow()

    fun onFetch() {
        _contractsState.value = ContractsState.Content
    }
}