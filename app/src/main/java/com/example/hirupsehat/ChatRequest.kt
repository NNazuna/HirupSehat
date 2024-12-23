package com.example.hirupsehat

data class ChatRequest(
    val model: String,
    val messages: List<ChatRequestMessage>
)
