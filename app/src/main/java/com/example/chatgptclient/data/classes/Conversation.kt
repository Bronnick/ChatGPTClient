package com.example.chatgptclient.data.classes

import androidx.room.*
import java.util.stream.Collectors


class ConversationConverter{

    @TypeConverter
    fun chatItemsStringToList(chatItemString: String): List<String>{
        return chatItemString.split("\n")
    }

    //strings like: "sometext00:00user"
    @TypeConverter
    fun listToChatItemsString(list: List<String>): String{
        return list.stream().collect(Collectors.joining("\n"))
    }
}

/*@Dao
interface conversationDao{
    @Query
    fun getConversation
}*/


@Entity
data class Conversation (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @TypeConverters(ConversationConverter::class)
    val chatItems: List<String>
)