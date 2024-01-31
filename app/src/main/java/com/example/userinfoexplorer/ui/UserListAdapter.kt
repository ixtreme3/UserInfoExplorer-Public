package com.example.userinfoexplorer.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userinfoexplorer.R
import com.example.userinfoexplorer.data.network.model.Result
import com.example.userinfoexplorer.databinding.ItemUserBinding

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    var callback: Callback? = null

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
            binding.apply {
                root.setOnClickListener { callback?.onItemClick(item) }
                userPicture.setImageResource(R.drawable.no_image)

                val fullName = "${item.name.first} ${item.name.last}"
                userName.text = fullName

                callback?.loadImage(item.picture.large, binding)

                val locationString = "${item.location.street.number} ${item.location.street.name}"
                val truncatedLocation =
                    if (locationString.length > 20) "${locationString.take(20)}.." else locationString
                location.text = truncatedLocation

                phoneNumber.text = item.phone
            }
        }
    }

    interface Callback {
        fun onItemClick(item: Result)
        fun loadImage(url: String, binding: ItemUserBinding)
    }
}