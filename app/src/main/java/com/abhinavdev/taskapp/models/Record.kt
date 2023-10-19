package com.abhinavdev.taskapp.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Record(
    @SerializedName("result")
    var result: Result? = null
): Serializable