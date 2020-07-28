package com.example.newsapp.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class News (
    @SerializedName("status") @Expose val status :String,
    @SerializedName("totalResult") @Expose val totalResult:Int,
    @SerializedName("articles") @Expose val articles : List<Article>
)
data class Source(
    @SerializedName("id") @Expose val id :String?,
    @SerializedName("name") @Expose val name:String?
)


@Entity(tableName = "articles")
data class Article(
    @SerializedName("source") @Expose
    @Embedded
    val source : Source,
    @SerializedName("author") @Expose val author: String?,
    @SerializedName("title") @Expose val title: String,
    @SerializedName("description") @Expose val description: String,
    @PrimaryKey
    @SerializedName("url") @Expose val url :String,
    @SerializedName("urlToImage") @Expose val urlToImage :String?,
    @SerializedName("publishedAt") @Expose val publishedAt:String
){
//    @PrimaryKey(autoGenerate = true)
//    val id:String? = null
}

