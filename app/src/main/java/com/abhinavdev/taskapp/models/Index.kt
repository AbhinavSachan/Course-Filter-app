package com.abhinavdev.taskapp.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Index(
    @SerializedName("downloadid")
    var downloadid: Int? = null,
    @SerializedName("cd_downloads")
    var cdDownloads: Int? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("release_date")
    var releaseDate: String? = null,
    @SerializedName("authorid")
    var authorid: Int? = null,
    @SerializedName("video_count")
    var videoCount: Int? = null,
    @SerializedName("style_tags")
    var styleTags: List<String?>? = null,
    @SerializedName("skill_tags")
    var skillTags: List<String?>? = null,
    @SerializedName("series_tags")
    var seriesTags: List<String?>? = null,
    @SerializedName("curriculum_tags")
    var curriculumTags: List<String?>? = null,
    @SerializedName("educator")
    var educator: String? = null,
    @SerializedName("owned")
    var owned: Int? = null,
    @SerializedName("sale")
    var sale: Int? = null,
//    @SerializedName("purchase_order")
//    var purchaseOrder: Int? = null,
    @SerializedName("watched")
    var watched: Int? = null,
    @SerializedName("progress_tracking")
    var progressTracking: Double? = null
): Serializable