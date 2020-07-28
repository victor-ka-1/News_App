package com.example.newsapp.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.newsapp.R
import com.example.newsapp.models.Article
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class NewsViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
    fun bind(article: Article){
        if(article.urlToImage != null){
            Glide.with(view.context)
                .load(article.urlToImage)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view.iv_article)
        } else{
            Glide.with(view.context)
                .load(R.drawable.ic_no_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view.iv_article)
        }


        view.tv_title.text = article.title
        view.tv_description.text = article.description

        view.tv_description.apply {
            setEllipsizedSuffixWithLink(3, "Show More", article.url)

            linksClickable = true
            movementMethod = LinkMovementMethod.getInstance()
            setLinkTextColor(Color.BLUE)
        }


        view.tv_time_item.text = article.publishedAt.replace("-",".").replace("T","  ").replace("Z","")
        view.tv_source.text = article.source.name

        view.tv_source.setOnClickListener {
            article.url.let {url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    companion object{
        fun create(parent : ViewGroup) : NewsViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
            return NewsViewHolder(view)
        }
    }
}