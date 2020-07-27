package com.example.newsapp.paging3

import androidx.paging.PagingSource
import com.example.newsapp.api.Article
import com.example.newsapp.api.NewsApiService
import retrofit2.HttpException
import java.io.IOException


private const val NEWS_STARTING_PAGE_INDEX = 1

class NewsPagingSource (
    private val apiService: NewsApiService,
    private val query:String
) : PagingSource<Int,Article>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: NEWS_STARTING_PAGE_INDEX
        val apiQuery = query
        val time = "2020-07-24"
        val ldSize = params.loadSize

        return try {
            val response = apiService.getNewsFor(q = query,fromTime = time,toTime = time, page = position)
            val articles = response.body()?.articles ?: emptyList()
            LoadResult.Page(
                data = articles,
                prevKey = null,
                nextKey = if(articles.isEmpty()) null else (position+1)
            )
        }catch (e: IOException){
            return LoadResult.Error(e)
        }catch (e:HttpException){
            return LoadResult.Error(e)
        }
    }

}