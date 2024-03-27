package com.example.githubmobile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubmobile.adapter.CoreAdapter
import com.example.githubmobile.data.model.UsersListItem
import com.example.githubmobile.databinding.FragmentFollowBinding
import com.example.githubmobile.model.FollowsViewModel
import com.example.githubmobile.utils.UtilsUsers

class FollowFragment : Fragment() {

    private var binding: FragmentFollowBinding? = null
    private val adapter by lazy {
        CoreAdapter {}
    }
    private val viewModel by activityViewModels<FollowsViewModel>()
    var target = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recylerViewFollow?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowFragment.adapter
        }

        when(target) {
            FOLLOWERS -> {
                viewModel.utilsFollowersUsers.observe(viewLifecycleOwner, this::handleFollowsResult)
            }
            FOLLOWING -> {
                viewModel.utilsFollowingUsers.observe(viewLifecycleOwner, this::handleFollowsResult)

            }
        }
    }

    private fun handleFollowsResult(state: UtilsUsers) {
        when (state) {
            is UtilsUsers.Success<*> -> {
                adapter.setUsersData(state.data as MutableList<UsersListItem.ItemList>)
            }
            is UtilsUsers.Error -> {
                Toast.makeText(requireActivity(), state.exception?.message ?: "An error occurred", Toast.LENGTH_SHORT).show()
            }
            is UtilsUsers.Loading -> {
                binding?.progressFollow?.isVisible = state.isLoading
            }
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val FOLLOWERS = 100
        const val FOLLOWING = 101

        fun newInstance(target: Int) = FollowFragment().apply {
            this.target = target
        }
    }
}