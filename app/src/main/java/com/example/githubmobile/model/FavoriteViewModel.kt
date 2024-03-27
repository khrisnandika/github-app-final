package com.example.githubmobile.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubmobile.data.database.UsersRepository
import com.example.githubmobile.data.model.UsersListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(private val database: UsersRepository) : ViewModel() {


    val utilsFavoriteSuccess = MutableLiveData<Boolean>()
    val utilsFavoriteDelete = MutableLiveData<Boolean>()

    private var isFavorite = false


    // get favorite using floatingbutton
    fun getUsersFavorite(item: UsersListItem.ItemList?) {
        viewModelScope.launch {
            item?.let {
                val isFav = isFavorite // Simpan nilai isFavorite ke dalam variabel lokal
                withContext(Dispatchers.IO) {
                    if (isFav) {
                        database.userDao.delete(item)
                        utilsFavoriteDelete.postValue(true)
                    } else {
                        database.userDao.insert(item)
                        utilsFavoriteSuccess.postValue(true)
                    }
                }
            }
            isFavorite = !isFavorite // Pindahkan perubahan isFavorite ke luar korutin
        }
    }

    // get users click by id
    fun getById(id: Int, listenFav: () -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                database.userDao.getById(id)
            }
            if (user != null) {
                listenFav()
                isFavorite = true
            }
        }
    }

    // to show in list favorite users
    fun getListFavoriteUsers() = database.userDao.getAllUsers()

}