package com.example.newsapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import kotlinx.android.synthetic.main.news_loadstate_footer_view_item.view.*

class NewsLoadStateViewHolder (
    private val view : View,
    retry : () -> Unit
) : RecyclerView.ViewHolder(view){
    init {
        view.btn_retry.setOnClickListener {  retry.invoke()  }
    }

    fun bind(loadState: LoadState){
        if(loadState is LoadState.Error){
            Log.d("-------__-_--__--_-", " ${loadState.error.localizedMessage}")
            view.tv_error_msg.text = loadState.error.localizedMessage
        }
        view.progressBar_load_footer.isVisible = loadState is LoadState.Loading
        view.btn_retry.isVisible = loadState !is LoadState.Loading
        view.tv_error_msg.isVisible = loadState !is LoadState.Loading
    }

    companion object{
        fun create(parent : ViewGroup, retry: () -> Unit) : NewsLoadStateViewHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_loadstate_footer_view_item,parent,false)
            return NewsLoadStateViewHolder(view, retry)
        }
    }
}