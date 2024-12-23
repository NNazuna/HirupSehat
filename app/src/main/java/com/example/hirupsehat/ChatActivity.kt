package com.example.hirupsehat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hirupsehat.databinding.ActivityChatBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        chatAdapter = ChatAdapter(messages)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }

        // Send button action
        binding.sendButton.setOnClickListener {
            val userMessage = binding.editTextMessage.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                addMessage(ChatMessage(sender = "User", content = userMessage, isUser = true))
                binding.editTextMessage.text.clear()
                sendMessageToBot(userMessage)
            }
        }
    }

    private fun addMessage(message: ChatMessage) {
        messages.add(message)
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.recyclerView.scrollToPosition(messages.size - 1)
    }

    private fun sendMessageToBot(userMessage: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val chatRequest = ChatRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(ChatRequestMessage(role = "user", content = userMessage))
                )
                val response = RetrofitInstance.openAIService.getChatResponse(chatRequest)
                val botReply = response.choices.firstOrNull()?.message?.content?.trim()
                    ?: "Sorry, I didn't understand that."
                runOnUiThread {
                    addMessage(ChatMessage(sender = "Bot", content = botReply, isUser = false))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    addMessage(ChatMessage(sender = "Bot", content = "Failed to connect to the server.", isUser = false))
                }
            }
        }
    }
}
