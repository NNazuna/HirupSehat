package com.example.hirupsehat

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIService {

    @Headers(
        "Authorization: Bearer sk-c2CQI6wMJKLNfL7I54HbZVzl4z_0mrwli_PmfnyjuaT3BlbkFJLgpAcIBX0-sXcpmi-QRe7qyme3nvj-cZoummA3gakA",
        "Content-Type: application/json"
    )
    @POST("v1/chat/completions")
    suspend fun getChatResponse(
        @Body request: ChatRequest
    ): ChatResponse
}

