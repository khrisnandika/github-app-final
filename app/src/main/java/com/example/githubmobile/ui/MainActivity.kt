package com.example.githubmobile.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubmobile.model.MainViewModel
import com.example.githubmobile.R
import com.example.githubmobile.adapter.CoreAdapter
import com.example.githubmobile.data.model.UsersListItem
import com.example.githubmobile.databinding.ActivityMainBinding
import com.example.githubmobile.model.SettingViewModel
import com.example.githubmobile.model.factory.SettingViewModelFactory
import com.example.githubmobile.utils.SettingPreferences
import com.example.githubmobile.utils.UtilsUsers
import com.example.githubmobile.utils.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeViewModel by viewModels<MainViewModel>()
    private val coreAdapter by lazy {
        CoreAdapter { usersDetail ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra("items", usersDetail)
                startActivity(this)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        observeUserList()

        setupSearchView()

        val pref = SettingPreferences.getInstance(this.dataStore)
        val mainViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.recylerViewMain?.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = coreAdapter
        }
    }

    private fun observeUserList() {
        homeViewModel.utilsUsers.observe(this) { utilsUsers ->
            when (utilsUsers) {
                is UtilsUsers.Success<*> -> {
                    coreAdapter.setUsersData(utilsUsers.data as MutableList<UsersListItem.ItemList>)
                }
                is UtilsUsers.Error -> {
                    Toast.makeText(this, utilsUsers.exception?.message ?: "Error occurred", Toast.LENGTH_SHORT).show()
                }
                is UtilsUsers.Loading -> binding?.progressBar?.isVisible = utilsUsers.isLoading ?: false
            }
        }
        homeViewModel.getListUsers()
    }

    private fun setupSearchView() {
        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                homeViewModel.getSearchListUser(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    homeViewModel.resetSearch()
                }
                return false
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding!= null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite -> {
                Intent(this, FavoriteActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.setting -> {
                Intent(this, SettingActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}