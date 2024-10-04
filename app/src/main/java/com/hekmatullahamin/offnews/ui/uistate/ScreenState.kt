package com.hekmatullahamin.offnews.ui.uistate

/**
 * Represents the different states of the screen.
 */
sealed class ScreenState {
    /**
     * Indicates that the screen is currently loading data.
     */
    data object Loading : ScreenState()

    /**
     * Indicates that the screen has successfully loaded data.
     */
    data object Success : ScreenState()

    /**
     * Indicates that an error occurred while loading data.
     *
     * @property errorMessage The error message to display to the user.
     */
    data class Error(val errorMessage: String) : ScreenState()
}
