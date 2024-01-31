package com.example.userinfoexplorer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userinfoexplorer.data.network.model.Result
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val userRepository = Singletons.userRepository

    private val _userList = MutableLiveData<List<Result>>()
    val userList: LiveData<List<Result>> get() = _userList

    val userMap = HashMap<String, Result>()

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    val picasso: Picasso by lazy { Picasso.get() }

    private val pageNumber = 1
    private val usersToGenerate = 300
    private val seedLength = 5

    init {
        fetchRandomUserList(generateRandomString())
    }

    fun reloadUserList() {
        fetchRandomUserList(generateRandomString())
    }

    private fun fetchRandomUserList(seed: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userResponse = userRepository.getUserList(pageNumber, usersToGenerate, seed)
                _userList.postValue(userResponse.results)

                userResponse.results.forEach {
                    userMap[it.login.uuid] = it
                }
            } catch (e: Exception) {
                _errorState.postValue(e.message)
                e.printStackTrace()
            }
        }
    }

    private fun generateRandomString(): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..seedLength)
            .map { charset.random() }
            .joinToString("")
    }
}