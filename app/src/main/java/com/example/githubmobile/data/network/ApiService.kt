package com.example.githubmobile.data.network

import com.example.githubmobile.BuildConfig
import com.example.githubmobile.data.model.UsersDetailListItem
import com.example.githubmobile.data.model.UsersListItem
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {


    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUsers(@Header("Authorization") authorization: String = BuildConfig.TOKEN) : MutableList<UsersListItem.ItemList>

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun getSearchListUsers(@QueryMap params: Map<String, Any>) : UsersListItem

    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getDetailListUsers(@Path("username") username: String, @Header("Authorization") authorization: String = BuildConfig.TOKEN) : UsersDetailListItem


    @JvmSuppressWildcards
    @GET("users/{username}/followers")
    suspend fun getListFollowers(@Path("username") username: String, @Header("Authorization") authorization: String = BuildConfig.TOKEN): MutableList<UsersListItem.ItemList>

    @JvmSuppressWildcards
    @GET("users/{username}/following")
    suspend fun getListFollowing(@Path("username") username: String, @Header("Authorization") authorization: String = BuildConfig.TOKEN): MutableList<UsersListItem.ItemList>

}