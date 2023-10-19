package com.abhinavdev.taskapp.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Result(
    @SerializedName("index")
    var index: List<Index?>? = null,
    @SerializedName("collections")
    var collections: Collections? = null
): Serializable