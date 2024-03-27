package com.example.githubmobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.githubmobile.data.model.UsersListItem
import com.example.githubmobile.databinding.ListUsersBinding

class CoreAdapter(private val dataUsers: MutableList<UsersListItem.ItemList> = mutableListOf(),
                  private val clickListener: (UsersListItem.ItemList) -> Unit) : RecyclerView.Adapter<CoreAdapter.UsersViewHolder>() {

    fun setUsersData(data: MutableList<UsersListItem.ItemList>) {
        this.dataUsers.clear()
        this.dataUsers.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder =
        UsersViewHolder(ListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = dataUsers[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            clickListener(item)
        }
    }

    override fun getItemCount(): Int = dataUsers.size

    class UsersViewHolder(private val binding : ListUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : UsersListItem.ItemList) {

            binding.circleProfile.load(item.avatar_url) {
                transformations(CircleCropTransformation())
            }
            binding.txtUsernameCard.text = item.login

        }
    }

}