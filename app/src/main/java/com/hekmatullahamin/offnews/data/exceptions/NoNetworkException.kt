package com.hekmatullahamin.offnews.data.exceptions

/**
 * An exception that is thrown when there is no network connection available.
 *
 * This exception indicates that an operation that requires network access
 * failed because the device is not connected to the internet or the network
 * is unavailable.
 *
 * @param message The detail message. The detail message is saved for later
 * retrieval by the [Throwable.message] method. Defaults to "No internet connection available."
 * @param cause The cause (which is saved for later retrieval by the
 * [Throwable.cause] method).  (A `null` value is permitted, and indicates
 * that the cause is nonexistent or unknown.)
 */
class NoNetworkException(
    message: String = "No internet connection available",
    cause: Throwable? = null
) :
    Exception(message, cause)