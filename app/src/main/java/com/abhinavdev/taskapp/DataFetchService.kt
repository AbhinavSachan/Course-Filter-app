package com.abhinavdev.taskapp

import com.abhinavdev.taskapp.models.DataModel
import com.abhinavdev.taskapp.utils.Constants
import retrofit2.Call
import retrofit2.http.GET

interface DataFetchService {
    @GET(Constants.API_LINK)
    fun getData():Call<DataModel>
}