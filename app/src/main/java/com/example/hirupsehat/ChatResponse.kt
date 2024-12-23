package com.example.hirupsehat

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatResponseMessage
)

data class ChatResponseMessage(
    val role: String,
    val content: String
)
