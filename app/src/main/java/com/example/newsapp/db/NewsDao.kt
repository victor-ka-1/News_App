package com.example.newsapp.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.api.Article

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles:List<Article>)

    @Query("SELECT * FROM articles WHERE publishedAt LIKE :date ORDER BY publishedAt DESC")
    fun getNewsUntilDate(date:String): PagingSource<Int, Article>

    @Query("DELETE FROM articles")
    suspend fun clearArticles()
}