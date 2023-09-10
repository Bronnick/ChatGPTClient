package com.example.chatgptclient

import android.app.Application
import android.content.Context
import androidx.datastore.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chatgptclient.database.AppDatabase

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val currentUserColorParam = intPreferencesKey("user_color")
val currentBotColorParam = intPreferencesKey("bot_color")

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appDatabase = Room
            .databaseBuilder(this, AppDatabase::class.java, "database")
            .build()

        settings = applicationContext.dataStore
    }

    companion object{

        lateinit var appDatabase: AppDatabase
            private set

        lateinit var settings: DataStore<Preferences>
            private set
    }
}