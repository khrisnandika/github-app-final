package com.example.githubmobile.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubmobile.data.network.ApiConfig
import com.example.githubmobile.utils.UtilsUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FollowsViewModel : ViewModel() {

    val utilsFollowersUsers = MutableLiveData<UtilsUsers>()
    val utilsFollowingUsers = MutableLiveData<UtilsUsers>()

    fun getDataFollowers(username: String) {
        viewModelScope.launch {
            utilsFollowersUsers.value = UtilsUsers.Loading(true)

            val response = try {
                withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getListFollowers(username)
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
                null
            }

            utilsFollowersUsers.value = UtilsUsers.Loading(false)

            response?.let {
                utilsFollowersUsers.value = UtilsUsers.Success(it)
            } ?: run {
                utilsFollowersUsers.value = UtilsUsers.Error(Exception("Failed to fetch followers"))
            }
        }
    }

    fun getDataFollowing(username: String) {
        viewModelScope.launch {
            utilsFollowingUsers.value = UtilsUsers.Loading(true)

            val response = try {
                withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getListFollowing(username)
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
                null
            }

            utilsFollowingUsers.value = UtilsUsers.Loading(false)

            response?.let {
                utilsFollowingUsers.value = UtilsUsers.Success(it)
            } ?: run {
                utilsFollowingUsers.value = UtilsUsers.Error(Exception("Failed to fetch following users"))
            }
        }
    }



}