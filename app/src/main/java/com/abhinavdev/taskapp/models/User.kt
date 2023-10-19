package com.abhinavdev.taskapp.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("label")
    var label: String? = null,
    @SerializedName("is_default")
    var isDefault: Int? = null,
    @SerializedName("is_archive")
    var isArchive: Int? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("courses")
    var courses: List<Int?>? = null
): Serializable