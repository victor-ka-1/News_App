package com.example.newsapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import kotlinx.android.synthetic.main.separator_view_item.view.*

class SeparatorViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
    fun bind(separatorText:String){
        view.separator_description.text = separatorText
    }
    companion object{
        fun create(parent:ViewGroup):SeparatorViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.separator_view_item, parent,false)
            return SeparatorViewHolder(view)
        }
    }

}