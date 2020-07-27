package com.example.newsapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("everything")
    suspend fun getNewsFor(
        @Query("from") fromTime :String,
        @Query("to") toTime :String,
        @Query("sortBy") sortBy:String = "publishedAt",
        @Query("page") page : Int?=null,
        @Query("pageSize") pageSize:Int = 10,
        @Query("language")language:String?="en",
        @Query("q") q:String?=null
    ) :  Response <News>

}