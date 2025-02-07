package com.example.thenewsapp.util

/**
 * A sealed class that encapsulates the state of network requests and data operations.
 * Provides type-safe handling of Success, Error, and Loading states.
 *
 * Features:
 * - Generic type support for any data type
 * - Null-safe data handling
 * - Optional error messages
 * - Sealed class hierarchy for exhaustive when expressions
 *
 * @param T The type of data being handled
 * @property data Optional data of type T
 * @property message Optional error message
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Represents a successful operation with data.
     *
     * @param data The data that was successfully retrieved/processed
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents an error state with an optional error message and data.
     *
     * @param message The error message describing what went wrong
     * @param data Optional data that might be available even in error state
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * Represents a loading state while waiting for an operation to complete.
     * Contains no data or message.
     */
    class Loading<T> : Resource<T>()
}