package dev.bittim.valolink.core.domain

typealias RootError = Error

sealed interface Result<out D, out E : RootError> {
    data class Loading<out D, out E : RootError>(val data: D? = null) : Result<D, E>
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>
    data class Error<out D, out E : RootError>(val error: E) : Result<D, E>
}