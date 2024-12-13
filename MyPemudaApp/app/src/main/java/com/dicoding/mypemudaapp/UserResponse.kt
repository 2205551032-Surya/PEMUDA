package com.dicoding.mypemudaapp

data class UserResponse(
    val message: String,
    val user: User?
)

data class User(
    val email: String,
    val token: String?
)