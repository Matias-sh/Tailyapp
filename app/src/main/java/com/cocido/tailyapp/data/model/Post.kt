package com.cocido.tailyapp.data.model

data class Post(
    val user: String,
    val location: String,
    val tag: String,
    val text: String,
    val imageUrl: String,
    val likes: Int
)
