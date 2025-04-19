package com.escom.myapplication.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Long = 0,
    val email: String = "",
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("lastName")
    val lastName: String = "",
    val role: String = "USER",
    val password: String = "",
    val profilePhoto: String? = null, // URL of the profile photo
    val isAdmin: Boolean = false
)