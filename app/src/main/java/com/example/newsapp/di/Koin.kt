package com.example.newsapp.di

import androidx.room.Room
import com.example.newsapp.api.NewsApiClient
import com.example.newsapp.api.NewsApiService
import com.example.newsapp.data.NewsRepository
import com.example.newsapp.db.NewsDataBase
import com.example.newsapp.ui.NewsViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val roomDBmodule = module {
    single { Room.databaseBuilder(  get(), NewsDataBase::class.java, "news_database").build() }
    single { get<NewsDataBase>().articleDao() }
    single { get<NewsDataBase>().remoteKeysDao() }
}

val viewModelModule = module {
    viewModel { NewsViewModel(repository = get()) }
}

val appModule = module {
    single { NewsRepository( apiService = get() , newsDataBase = get()) }
}

val apiModule = module {
    single { NewsApiClient.okHttpClient() }
    single { NewsApiClient.getApiClient( okHttpClient = get() ) }
    single { NewsApiClient.getApiService( retrofit = get() )  }
}