package com.example.userinfoexplorer.data

import com.example.userinfoexplorer.data.network.RandomUserApi
import com.example.userinfoexplorer.data.network.model.UserResponse

class UserRepository(private val api: RandomUserApi) {
    suspend fun getUserList(page: Int, results: Int, seed: String): UserResponse {
        return api.getUserList(page, results, seed)
    }
}
