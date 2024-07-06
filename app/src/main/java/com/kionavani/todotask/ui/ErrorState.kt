package com.kionavani.todotask.ui

interface ErrorState {
    class FetchingError(message: String? = null) : ErrorState
    class UpdatingError(message: String? = null) : ErrorState
    class ErrorProcessed() : ErrorState
}