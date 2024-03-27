package com.example.githubmobile.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubmobile.adapter.CoreAdapter
import com.example.githubmobile.data.database.UsersRepository
import com.example.githubmobile.databinding.ActivityFavoriteBinding
import com.example.githubmobile.model.FavoriteViewModel
import com.example.githubmobile.model.factory.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val usersRepository by lazy { UsersRepository(this) }
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory(usersRepository)
    }

    private val coreAdapter by lazy {
        CoreAdapter { usersFavorites ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra("items", usersFavorites)
                startActivity(this)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recylerViewFav.layoutManager = LinearLayoutManager(this)
        binding.recylerViewFav.adapter = coreAdapter

    }

    override fun onResume() {
        super.onResume()
        // Reloads data every time the Fragment is displayed
        loadFavoriteData()
    }

    private fun loadFavoriteData() {
        // Reload data from ViewModel
        favoriteViewModel.getListFavoriteUsers().observe(this) { newData ->
            coreAdapter.setUsersData(newData)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}