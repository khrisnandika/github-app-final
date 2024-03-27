package com.example.githubmobile.utils

sealed class UtilsUsers {
    data class Success<out T>(val data: T) : UtilsUsers()
    data class Error(val exception: Throwable?) : UtilsUsers()
    data class Loading(val isLoading: Boolean) : UtilsUsers()
}