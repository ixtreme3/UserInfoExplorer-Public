package com.example.userinfoexplorer.ui.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userinfoexplorer.data.network.model.Result
import com.example.userinfoexplorer.Singletons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListViewModel : ViewModel() {
    private val userRepository = Singletons.userRepository

    private val _userList = MutableLiveData<List<Result>>()
    val userList: LiveData<List<Result>> get() = _userList

    fun fetchUserList(page: Int, results: Int, seed: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userResponse = userRepository.getUserList(page, results, seed)
                _userList.postValue(userResponse.results)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}