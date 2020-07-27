package com.example.newsapp.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.newsapp.api.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class NewsViewModel (private val repository: NewsRepository) : ViewModel(){
    private var currentQueryValue: String? = null

    private var currentNewsResult : Flow<PagingData<UiModel>>? = null

    fun getNewsFor(queryString:String, date :String): Flow<PagingData<UiModel>> {
        val lastResult = currentNewsResult
        if(queryString == currentQueryValue && lastResult != null){
            return lastResult
        }
        currentQueryValue = queryString
        val newResult : Flow<PagingData<UiModel>> = repository.getNewsUntilDateStream(queryString, date)
            .map { pagingData: PagingData<Article> -> pagingData.map { UiModel.ArticleItem(it) } }
            .map {
                it.insertSeparators<UiModel.ArticleItem,UiModel> { before, after ->
                    if(after == null) {
                        // we're at the end of the list
                        return@insertSeparators null
                    }
                    if(before == null){
                        // we're at the beginning of the list
                        return@insertSeparators UiModel.SeparatorItem("${after.timeAgo}")
                    }
                    // check between 2 items
                    if(before.timeAgo!= null && after.timeAgo!=null && before.timeAgo != after.timeAgo){
                            UiModel.SeparatorItem("${after.timeAgo}")
                    } else { // no separator
                        null
                    }
                }

            }.cachedIn(viewModelScope)
        currentNewsResult = newResult
        return newResult
    }
}


sealed class UiModel{
    data class ArticleItem(val article: Article):UiModel()
    data class SeparatorItem(val description: String):UiModel()
}
private val UiModel.ArticleItem.timeAgo: String?
get() {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val date =simpleDateFormat.parse( this.article.publishedAt)
    val publishedTime = Calendar.getInstance()
    date?.let {
        publishedTime.timeInMillis = (date.time)
        val now = Calendar.getInstance()
        val daysDiff = now.get(Calendar.DATE) - publishedTime.get(Calendar.DATE)
        if(daysDiff == 0 ) return "Today's news"
        if(daysDiff == 1) return "Yesterday's news"
        return "News for $daysDiff days ago"
    }
    return null
}


