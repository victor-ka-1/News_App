package com.example.newsapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val articleUrl:String,
    val previousKey:Int?,
    val nextKey:Int?
)