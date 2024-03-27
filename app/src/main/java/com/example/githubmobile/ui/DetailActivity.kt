package com.example.githubmobile.ui

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.example.githubmobile.R
import com.example.githubmobile.adapter.SectionPagerAdapter
import com.example.githubmobile.data.database.UsersRepository
import com.example.githubmobile.data.model.UsersDetailListItem
import com.example.githubmobile.data.model.UsersListItem
import com.example.githubmobile.databinding.ActivityDetailBinding
import com.example.githubmobile.model.DetailScreenViewModel
import com.example.githubmobile.model.FavoriteViewModel
import com.example.githubmobile.model.FollowsViewModel
import com.example.githubmobile.model.factory.ViewModelFactory
import com.example.githubmobile.utils.UtilsUsers
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailScreenViewModel by viewModels<DetailScreenViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel>() {
        ViewModelFactory(UsersRepository(this))
    }
    private val followsViewModel by viewModels<FollowsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val getUsersDetail = intent.getParcelableExtra<UsersListItem.ItemList>("items")
        val nameUsersDetail = getUsersDetail?.login ?: ""

        detailScreenViewModel.utilsUsersDetail.observe(this) { utilsUsers ->
            when (utilsUsers) {
                is UtilsUsers.Success<*> -> {
                    val detailUsers = utilsUsers.data as UsersDetailListItem
                    binding.detailProfile.load(detailUsers.avatar_url) {
                        transformations(CircleCropTransformation())
                    }
                    binding.textDetailName.text = detailUsers.name
                    binding.textDetailUsername.text = detailUsers.login
                    binding.textTotalRepo.text = detailUsers.public_repos.toString()
                    binding.textTotalFollowers.text = detailUsers.followers.toString()
                    binding.textTotalFollowing.text = detailUsers.following.toString()
                }
                is UtilsUsers.Error -> {
                    Toast.makeText(this, utilsUsers.exception?.message ?: "Error occurred", Toast.LENGTH_SHORT).show()
                }
                is UtilsUsers.Loading -> binding?.progresUsersdetail?.isVisible = utilsUsers.isLoading ?: false
            }
        }
        detailScreenViewModel.getListUsersDetails(nameUsersDetail)

        val fragments = mutableListOf<Fragment>(
            FollowFragment.newInstance(FollowFragment.FOLLOWERS),
            FollowFragment.newInstance(FollowFragment.FOLLOWING)
        )

        val titleFragments = mutableListOf(
            getString(R.string.tab_text_1),
            getString(R.string.tab_text_2)
        )

        val adapter = SectionPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = titleFragments[position]
        }.attach()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    followsViewModel.getDataFollowers(nameUsersDetail)
                } else {
                    followsViewModel.getDataFollowing(nameUsersDetail)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        followsViewModel.getDataFollowers(nameUsersDetail)

        binding.btnFav.setOnClickListener {
            favoriteViewModel.getUsersFavorite(getUsersDetail)
        }

        favoriteViewModel.utilsFavoriteSuccess.observe(this) {
            binding.btnFav.changeIconColor(R.color.red)
        }

        favoriteViewModel.utilsFavoriteDelete.observe(this) {
            binding.btnFav.changeIconColor(R.color.white)
        }

        favoriteViewModel.getById(getUsersDetail?.id ?: 0) {
            binding.btnFav.changeIconColor(R.color.red)
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

fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}
