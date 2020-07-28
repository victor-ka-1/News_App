package com.example.newsapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.ui.NewsAdapter
import com.example.newsapp.ui.NewsLoadStateAdapter
import com.example.newsapp.ui.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
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

        val query = "belarus"
        requestNews(query)


        swipeToRefreshLayout.setOnRefreshListener {
            myAdapter.refresh()
        }

        myAdapter.addDataRefreshListener {
            swipeToRefreshLayout.isRefreshing = false
            recyclerView.scrollToPosition(0)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.news_options_menu,menu)

        val searchItem =menu!!.findItem(R.id.searchItem)
        val searchView =searchItem.actionView as SearchView
        searchView.imeOptions =EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(text: String?): Boolean {
                requestJob?.cancel()
                requestJob = lifecycleScope.launch {
                     myViewModel.filterShowedNews(text).collectLatest {
                        myAdapter.submitData(it)
                    }
                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_refreshList ->{
                myAdapter.refresh()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initAdapter() {
        recyclerView.adapter = myAdapter.withLoadStateFooter( footer = NewsLoadStateAdapter{ myAdapter.retry() })
    }


    private fun requestNews(query:String){
        requestJob?.cancel()
        requestJob = lifecycleScope.launch {
            myViewModel.getNewsFor(query, "2020-07-25").collectLatest {
                myAdapter.submitData(it)
            }
            recyclerView.scrollToPosition(0)
        }
    }
}