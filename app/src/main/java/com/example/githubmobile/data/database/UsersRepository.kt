package com.example.githubmobile.data.database

import android.content.Context
import androidx.room.Room

class UsersRepository(private val context: Context) {
    private val db = Room.databaseBuilder(context, UsersRoomDatabase::class.java, "userstest.db")
        .allowMainThreadQueries()
        .build()
    val userDao = db.usersDao()

}