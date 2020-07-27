package com.example.newsapp.paging3

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.api.Article
import com.example.newsapp.api.NewsApiService
import com.example.newsapp.db.NewsDataBase
import kotlinx.coroutines.flow.Flow

class NewsRepository(private val apiService: NewsApiService,
                     private val newsDataBase: NewsDataBase){
    companion object{
        private const val NETWORK_PAGE_SIZE=10
    }

//    fun getRequestResultStream( query:String ) : Flow<PagingData<Article>>{
//        return Pager(
//            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
//            pagingSourceFactory = { NewsPagingSource(apiService, query=query) }
//        ).flow
//    }



    fun getNewsUntilDateStream(query:String, date:String) : Flow<PagingData<Article>> {
        val pagingSourceFactory = { newsDataBase.articleDao().getNewsUntilDate(date) }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator =NewsRemoteMediator(
                query,
                date,
                apiService,
                newsDataBase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}