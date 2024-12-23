package com.example.hirupsehat

data class Mission(
    val title: String,
    val description: String,
    val points: Int,
    var isCompleted: Boolean = false
)
