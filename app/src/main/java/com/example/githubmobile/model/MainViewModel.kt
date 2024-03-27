package com.example.githubmobile.model

import android.util.Log
import androidx.lifecycle.*
import com.example.githubmobile.data.network.ApiConfig
import com.example.githubmobile.utils.UtilsUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel() : ViewModel() {

    val utilsUsers = MutableLiveData<UtilsUsers>()

    fun getListUsers() {
        viewModelScope.launch {
            utilsUsers.value = UtilsUsers.Loading(true)

            val response = try {
                withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getUsers()
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
                null
            }

            utilsUsers.value = UtilsUsers.Loading(false)

            response?.let {
                utilsUsers.value = UtilsUsers.Success(it)
            } ?: run {
                utilsUsers.value = UtilsUsers.Error(Exception("Failed to fetch users"))
            }
        }
    }


    fun getSearchListUser(username: String) {
        viewModelScope.launch {
            utilsUsers.value = UtilsUsers.Loading(true)

            val response = try {
                withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getSearchListUsers(
                        mapOf(
                            "q" to username,
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
                null
            }

            utilsUsers.value = UtilsUsers.Loading(false)

            response?.let {
                utilsUsers.value = UtilsUsers.Success(it.items)
            } ?: run {
                utilsUsers.value = UtilsUsers.Error(Exception("Failed to fetch users"))
            }
        }
    }


    fun resetSearch() {
        getListUsers()
    }
}