package com.example.chatgptclient.data.classes

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query


@Dao
interface ChatItemDao{

    @Query("SELECT * FROM chatitem where conversation_name = :conversationName")
    fun getConversation(conversationName: String): List<ChatItem>

    @Query("SELECT conversation_name FROM chatitem GROUP BY conversation_name")
    fun getConversationNames(): List<String>

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

    @ColumnInfo(name = "conversation_name")
    val conversationName: String
)