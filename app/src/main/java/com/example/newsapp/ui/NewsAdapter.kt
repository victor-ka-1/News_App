package com.example.newsapp.ui

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.StringUtil
import com.example.newsapp.R

class NewsAdapter: PagingDataAdapter<UiModel,RecyclerView.ViewHolder>(DiffUtilCallback){
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when(uiModel){
                is UiModel.ArticleItem -> (holder as NewsViewHolder).bind(uiModel.article)
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(uiModel.description)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is UiModel.ArticleItem -> R.layout.recyclerview_item
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == R.layout.recyclerview_item){
            return NewsViewHolder.create(parent)
        }else{
            return SeparatorViewHolder.create(parent)
        }

    }



}

object DiffUtilCallback :DiffUtil.ItemCallback<UiModel>(){
    override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        if(oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem){
            return oldItem.description == newItem.description
        }
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        if(oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem){
            return oldItem.description == newItem.description
        }
        return (oldItem is UiModel.ArticleItem && newItem is UiModel.ArticleItem
                && oldItem.article.urlToImage == newItem.article.urlToImage
                && oldItem.article.author == newItem.article.author
                && oldItem.article.description == newItem.article.description
                && oldItem.article.publishedAt == newItem.article.publishedAt)
                || (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem
                && oldItem.description == newItem.description)
    }


}