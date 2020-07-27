package com.example.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.ui.NewsAdapter
import com.example.newsapp.ui.NewsLoadStateAdapter
import com.example.newsapp.paging3.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val myViewModel: NewsViewModel by viewModel()
    private  val myAdapter :NewsAdapter =  NewsAdapter()
    private var requestJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rwLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = rwLayoutManager
        initAdapter()

        val query = "android"
        requestNews(query)

    }

    private fun initAdapter() {
        recyclerView.adapter = myAdapter.withLoadStateFooter( footer = NewsLoadStateAdapter{ myAdapter.retry() })
    }

    private fun initRequest(query: String){
        lifecycleScope.launch {

            myAdapter.dataRefreshFlow.collect {

            }
        }
    }

    private fun requestNews(query:String){
        requestJob?.cancel()
        requestJob = lifecycleScope.launch {
            myViewModel.getNewsFor(query, "2020-07-26").collectLatest {
                myAdapter.submitData(it)
            }
        }
    }


//    private fun updateRepoListFromInput() {
//        binding.searchRepo.text.trim().let {
//            if (it.isNotEmpty()) {
//                binding.list.scrollToPosition(0)
//                search(it.toString())
//            }
//        }
//    }
}