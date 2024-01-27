package com.example.userinfoexplorer.network

import com.example.userinfoexplorer.network.model.UserResponse

class UserRepository(private val api: RandomUserApi) {
    suspend fun getUserList(page: Int, results: Int, seed: String): UserResponse {
        return api.getUserList(page, results, seed)
    }
}
