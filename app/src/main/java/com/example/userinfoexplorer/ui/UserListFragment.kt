package com.example.userinfoexplorer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.userinfoexplorer.MainActivity
import com.example.userinfoexplorer.R
import com.example.userinfoexplorer.MainViewModel
import com.example.userinfoexplorer.data.network.model.Result
import com.example.userinfoexplorer.databinding.FragmentUserListBinding
import com.example.userinfoexplorer.databinding.ItemUserBinding
import io.github.muddz.styleabletoast.StyleableToast

class UserListFragment : Fragment(), UserListAdapter.Callback {
    private lateinit var binding: FragmentUserListBinding

    private val viewModel: MainViewModel by activityViewModels()

    private val userListAdapter = UserListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)

        manageSpinnerVisibility(View.VISIBLE)

        binding.recyclerView.adapter = userListAdapter
        viewModel.userList.observe(viewLifecycleOwner) { newData ->
            userListAdapter.data = newData
            manageSpinnerVisibility(View.GONE)
            binding.swipeRefreshLayout.isRefreshing = false
        }
        userListAdapter.callback = this

        viewModel.errorState.observe(viewLifecycleOwner) { error ->
            StyleableToast.makeText(
                requireContext(), "Ошибка загрузки списка: \n $error",
                Toast.LENGTH_LONG, R.style.error_toast
            ).show()
            manageSpinnerVisibility(View.GONE)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.reloadUserList()
        }

        return binding.root
    }

    private fun manageSpinnerVisibility(visibility: Int) {
        val activity = activity as MainActivity
        val progressBar = activity.findViewById<ProgressBar>(R.id.spinner)
        progressBar.visibility = visibility
    }

    override fun loadImage(url: String, binding: ItemUserBinding) {
        viewModel.picasso
            .load(url)
            .placeholder(R.drawable.no_image)
            .error(R.drawable.no_image)
            .into(binding.userPicture)
    }

    override fun onItemClick(item: Result) {
        parentFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container_view, UserFragment.newInstance(item.login.uuid))
            .commit()
    }
}