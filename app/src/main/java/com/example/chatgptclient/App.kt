package com.example.chatgptclient

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chatgptclient.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        appDatabase = Room
            .databaseBuilder(this, AppDatabase::class.java, "database")
            .build()
    }

    companion object{

        lateinit var instance: Application

        lateinit var appDatabase: AppDatabase
    }
}