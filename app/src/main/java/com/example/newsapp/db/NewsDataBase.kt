package com.example.newsapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.models.Article

@Database(entities = [Article::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class NewsDataBase : RoomDatabase(){
    abstract fun articleDao(): NewsDao
    abstract fun remoteKeysDao():RemoteKeysDao
}