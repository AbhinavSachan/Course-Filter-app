package com.abhinavdev.taskapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhinavdev.taskapp.DataFetchService
import com.abhinavdev.taskapp.Module
import com.abhinavdev.taskapp.R
import com.abhinavdev.taskapp.models.DataModel
import com.abhinavdev.taskapp.ui.adapters.CollectionAdaptor
import com.abhinavdev.taskapp.utils.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var collectionRecView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val backBtn: View = findViewById(R.id.back_btn)
        backBtn.visibility = View.GONE


        collectionRecView = findViewById(R.id.collection_rec_view)
        progressBar = findViewById(R.id.progress_bar)
        collectionRecView?.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        collectionRecView?.layoutManager = layoutManager

        getData()
    }

    private fun getData() {
        val service = ApiClient.getClient()?.create(DataFetchService::class.java)
        val call = service?.getData()
        call?.enqueue(object : Callback<DataModel> {
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                if (response.code() == 200) {
                    val result = response.body()?.record?.result
                    Module.setData(response.body())
                    collectionRecView?.adapter = CollectionAdaptor(result, this@MainActivity)
                    progressBar?.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<DataModel>, t: Throwable) {
                progressBar?.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }
}