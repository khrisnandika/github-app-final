package com.example.githubmobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubmobile.data.model.UsersListItem

@Database(entities = [UsersListItem.ItemList::class], version = 1, exportSchema = false)
abstract class UsersRoomDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao


}