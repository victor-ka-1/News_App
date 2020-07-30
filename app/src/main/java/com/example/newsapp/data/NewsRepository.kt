package com.example.newsapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.models.Article
import com.example.newsapp.api.NewsApiService
import com.example.newsapp.db.NewsDataBase
import kotlinx.coroutines.flow.Flow
import java.util.*

class NewsRepository(private val apiService: NewsApiService,
                     private val newsDataBase: NewsDataBase){
    companion object{
        private const val NETWORK_PAGE_SIZE=20
    }

    fun getNewsUntilDateStream(query:String) : Flow<PagingData<Article>> {
        val pagingSourceFactory = { newsDataBase.articleDao().getNewsUntilDate() }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator =NewsRemoteMediator(
                query = query,
                fromDate = calculateDate(),
                toDate = formatToString( Calendar.getInstance() ),
                apiService = apiService,
                newsDataBase = newsDataBase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    private fun calculateDate(): String {
        val dayOffset = 24 * 60 * 60 * 1000 * 7 //7 days in millis
        val myDate = Calendar.getInstance()
        myDate.timeInMillis = (myDate.timeInMillis - dayOffset)
        return formatToString(myDate)
    }
    fun formatToString(myDate:Calendar): String {
        val year = myDate.get(Calendar.YEAR)
        val month = myDate.get(Calendar.MONTH)+1
        val day_of_month = myDate.get(Calendar.DAY_OF_MONTH)

        val monthString =if(month <10) "0${month}" else month.toString()
        val dayMonthString = if(day_of_month < 10) "0${day_of_month}" else day_of_month.toString()

        val dateString =  "${year}-${monthString}-${dayMonthString}"
        return dateString
    }
}