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

class DetailScreenViewModel : ViewModel() {


    val utilsUsersDetail = MutableLiveData<UtilsUsers>()

    fun getListUsersDetails(username: String) {
        viewModelScope.launch {
            utilsUsersDetail.value = UtilsUsers.Loading(true)

            val response = try {
                withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getDetailListUsers(username)
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
                null
            }

            utilsUsersDetail.value = UtilsUsers.Loading(false)

            response?.let {
                utilsUsersDetail.value = UtilsUsers.Success(it)
            } ?: run {
                utilsUsersDetail.value = UtilsUsers.Error(Exception("Failed to fetch user details"))
            }
        }
    }



}