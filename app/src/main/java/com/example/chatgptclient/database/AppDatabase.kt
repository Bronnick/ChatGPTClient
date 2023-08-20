package com.example.chatgptclient.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatgptclient.data.classes.ChatItem
import com.example.chatgptclient.data.classes.ChatItemDao



@Database(entities = [ChatItem::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getChatItemDao(): ChatItemDao
}