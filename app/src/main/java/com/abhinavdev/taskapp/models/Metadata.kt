package com.abhinavdev.taskapp.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Metadata(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("private")
    var `private`: Boolean? = null,
    @SerializedName("createdAt")
    var createdAt: String? = null
): Serializable