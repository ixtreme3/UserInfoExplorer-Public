package com.example.userinfoexplorer.ui.userlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userinfoexplorer.R
import com.example.userinfoexplorer.data.network.model.Result
import com.example.userinfoexplorer.databinding.ItemUserBinding

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    var data = listOf<Result>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Result) {
            binding.userPicture.setImageResource(R.drawable.no_image)
            binding.userName.text = "${item.name.first} ${item.name.last}"
            binding.location.text = "${item.location.street.number} ${item.location.street.name}"
            binding.phoneNumber.text = item.phone
        }
    }
}