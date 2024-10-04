package com.hekmatullahamin.offnews.data.exceptions

/**
 * An exception that is thrown when an article is empty or not found.
 *
 * This exception indicates that an attempt was made to access or use an article
 * that is either empty or does not exist.
 *
 * @param message The detail message. The detail message is saved for later
 * retrieval by the [Throwable.message] method.
 */
class EmptyArticleException(message: String = "Article not found. it is empty") : Exception(message)