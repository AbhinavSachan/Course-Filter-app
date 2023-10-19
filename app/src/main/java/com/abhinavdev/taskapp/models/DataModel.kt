package com.abhinavdev.taskapp.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataModel(
    @SerializedName("record")
    var record: Record? = null,
    @SerializedName("metadata")
    var metadata: Metadata? = null
): Serializable