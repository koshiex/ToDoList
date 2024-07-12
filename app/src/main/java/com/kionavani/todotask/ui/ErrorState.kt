package com.kionavani.todotask.ui

/**
 * Sealed интерфейс, представляющий различные состояния ошибок при работе с данными
 */
interface ErrorState {
    /**
     * Состояние ошибки, возникающей при получении данных
     **/
    class FetchingError(message: String? = null) : ErrorState
    /**
     * Состояние ошибки, возникающей при обновлении данных
     **/
    class UpdatingError(message: String? = null) : ErrorState
    /**
     * Состояние, когда все ошибки обработаны
     **/
    class ErrorProcessed() : ErrorState
}