package com.example.githubmobile.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubmobile.data.model.UsersListItem

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: UsersListItem.ItemList)

    @Update
    fun update(users: UsersListItem.ItemList)


    @Query("SELECT * FROM Users")
    fun getAllUsers(): LiveData<MutableList<UsersListItem.ItemList>>

    @Query("SELECT * FROM Users WHERE id LIKE :id LIMIT 1")
    fun getById(id: Int): UsersListItem.ItemList

    @Delete
    fun delete(user: UsersListItem.ItemList)

    @Query("DELETE FROM Users WHERE id = :id")
    fun deleteById(id: Int)
}