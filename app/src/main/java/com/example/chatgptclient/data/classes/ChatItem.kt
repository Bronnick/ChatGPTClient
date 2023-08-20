package com.example.chatgptclient.data.classes

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query


@Dao
interface ChatItemDao{

    @Query("SELECT * FROM chatitem where conversationName = :conversationName")
    fun getConversation(conversationName: String): List<ChatItem>

    @Insert
    fun addChatItem(chatItem: ChatItem)
}

@Entity
data class ChatItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val text: String,

    val time: String,

    val role: String,

    val conversationName: String
)