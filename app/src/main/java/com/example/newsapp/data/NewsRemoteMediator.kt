package com.example.newsapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsapp.models.Article
import com.example.newsapp.api.NewsApiService
import com.example.newsapp.db.NewsDataBase
import com.example.newsapp.db.RemoteKeys
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

private const val NEWS_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator (
    private val query:String,
    private val toDate :String,
    private val fromDate :String,
    private val apiService: NewsApiService,
    private val newsDataBase: NewsDataBase
) : RemoteMediator<Int, Article>(){
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Article>): MediatorResult {
        val page:Int = when(loadType){
            LoadType.REFRESH -> {
//                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                remoteKeys?.nextKey?.minus(1) ?: NEWS_STARTING_PAGE_INDEX
                NEWS_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND ->{
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                if (remoteKeys == null) {
//                    // The LoadType is PREPEND so some data was loaded before,
//                    // so we should have been able to get remote keys
//                    // If the remoteKeys are null, then we're an invalid state and we have a bug
//                    throw InvalidObjectException("Remote key and the prevKey should not be null")
//                }
//                // If the previous key is null, then we can't request more data
//                 remoteKeys.previousKey ?:
                 return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys == null){
                    NEWS_STARTING_PAGE_INDEX + 1
                }else {
                    if (remoteKeys.nextKey == null) {
                        throw InvalidObjectException("Remote key should not be null for $loadType")
                    }
                    remoteKeys.nextKey
                }
            }
        }

        try {
            val apiResponse =
                apiService.getNewsFor(q = query, fromTime = fromDate, toTime = toDate,page =page, pageSize = 20)
            val articles = apiResponse.body()?.articles ?: emptyList()
            val endOfPaginationReached = articles.isEmpty()
            newsDataBase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    newsDataBase.remoteKeysDao().clearRemoteKeys()
                    newsDataBase.articleDao().clearArticles()
                }
                val previousKey = null /*if( page == NEWS_STARTING_PAGE_INDEX) null else (page - 1)*/
                val nextKey = if(endOfPaginationReached) null else (page + 1)
                val keys = articles.map {
                    RemoteKeys(articleUrl = it.url,previousKey = previousKey, nextKey = nextKey)
                }
                newsDataBase.remoteKeysDao().insertAll(keys)
                newsDataBase.articleDao().insertAll(articles)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        }catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }



    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>) :RemoteKeys?{
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {article ->
                // Get the remote keys of the last item retrieved
                newsDataBase.remoteKeysDao().remoteKeysArticleUrl(article.url)
            }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>):RemoteKeys?{
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { article ->
                // Get the remote keys of the first items retrieved
                newsDataBase.remoteKeysDao().remoteKeysArticleUrl(article.url)
            }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Article>): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { articleUrl ->
                newsDataBase.remoteKeysDao().remoteKeysArticleUrl(articleUrl)
            }
        }
    }

}