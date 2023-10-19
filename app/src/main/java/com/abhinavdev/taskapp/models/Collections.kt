package com.abhinavdev.taskapp.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Collections(
    @SerializedName("smart")
    var smart: List<Smart?>? = null,
    @SerializedName("user")
    var user: List<User?>? = null,
    @SerializedName("curated")
    var curated: List<Any?>? = null
):Serializable