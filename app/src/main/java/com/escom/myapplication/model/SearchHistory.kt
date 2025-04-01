package com.escom.myapplication.model

import com.google.gson.annotations.SerializedName

data class SearchHistory(
    val id: Long? = null,
    @SerializedName("userId")
    val userId: Long = 0,
    @SerializedName("searchTerm")
    val searchTerm: String = "",
    val timestamp: String = ""
)