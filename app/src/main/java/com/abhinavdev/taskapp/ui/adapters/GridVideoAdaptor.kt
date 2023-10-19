package com.abhinavdev.taskapp.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abhinavdev.taskapp.R
import com.abhinavdev.taskapp.models.FilterModel
import com.abhinavdev.taskapp.models.Index
import com.abhinavdev.taskapp.utils.Constants
import com.abhinavdev.taskapp.utils.CourseFilter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

class GridVideoAdaptor(val list: MutableList<Index?>, val context: Context) :
    RecyclerView.Adapter<GridVideoAdaptor.ViewHolder>() {

    private var filterList: MutableList<Index?> = mutableListOf()
    private var tempSearchList: MutableList<Index?> = mutableListOf()
    private var filterDiffList = mutableSetOf<Index?>()
    private var tempList: MutableList<Index?> = mutableListOf()
    private var tempEducatorList: MutableList<Index?> = mutableListOf()

    init {
        filterList = list
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.title_tv)
        val educatorText: TextView = view.findViewById(R.id.educator_tv)
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail_img)
        val ownedTxt: View = view.findViewById(R.id.owned_text_rl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.grid_video_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    fun search(query: String) {
        if (tempSearchList.isEmpty()) {
            tempSearchList = filterList
        }
        val diffList = mutableListOf<Index?>()
        for (index in tempSearchList) {
            if (index?.title?.lowercase()
                    ?.contains(query.lowercase()) == true || index?.educator?.lowercase()
                    ?.contains(query.lowercase()) == true
            ) {
                diffList.add(index)
            }
        }
        filterList = diffList
        notifyDataSetChanged()
    }

    fun clearSearch() {
        filterList = tempSearchList
        notifyDataSetChanged()
    }

    fun applyOwnedFilter() {
        val diffList = mutableListOf<Index?>()
        tempList = filterList
        for (index in list) {
            if (index?.owned == 1) {
                diffList.add(index)
            }
        }
        filterList = diffList
        notifyDataSetChanged()
    }

    fun applyEducatorFilter(educator: String?) {
        if (tempEducatorList.isNotEmpty()) {
            filterList = tempEducatorList
        }
        val diffList = mutableListOf<Index?>()
        tempEducatorList = filterList
        for (index in filterList) {
            if (index?.educator == educator) {
                diffList.add(index)
            }
        }
        filterList = diffList
        notifyDataSetChanged()
    }

    fun clearAllFilters() {
        filterDiffList.clear()
        filterList = list
        notifyDataSetChanged()
    }

    fun clearOwnedFilter() {
        filterList = tempList
        notifyDataSetChanged()
    }

    fun applyFilter(filters: LinkedHashMap<CourseFilter, FilterModel?>) {
        for (index in filterList) {
            filters.forEach { (t, u) ->
                when (t) {
                    CourseFilter.SKILL -> {
                        if (index?.skillTags?.contains(u?.filterName) == true) {
                            filterDiffList.add(index)
                        }
                    }

                    CourseFilter.STYLE -> {
                        if (index?.styleTags?.contains(u?.filterName) == true) {
                            filterDiffList.add(index)
                        }
                    }

                    CourseFilter.SERIES -> {
                        if (index?.seriesTags?.contains(u?.filterName) == true) {
                            filterDiffList.add(index)
                        }
                    }

                    CourseFilter.CURRICULUM -> {
                        if (index?.curriculumTags?.contains(u?.filterName) == true) {
                            filterDiffList.add(index)
                        }
                    }

                    else -> {
                        Log.d("TAG", "Something went wrong")
                    }
                }
            }
        }
        filterList = filterDiffList.toMutableList()
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = filterList[position]
        holder.titleText.text = video?.title
        holder.educatorText.text = video?.educator

        if (video?.owned == 1) {
            holder.ownedTxt.visibility = View.VISIBLE
        } else {
            holder.ownedTxt.visibility = View.GONE
        }

        val path = "${Constants.IMAGE_LINK}${video?.id}${Constants.IMAGE_NAME}"
        Log.d("IMAGE_LINK", path)
        val value =
            "Mozilla/5.0 (Linux; Android 11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.181 Mobile Safari/537.36"

        val glideUrl = GlideUrl(
            path,
            LazyHeaders.Builder().addHeader("User-Agent", value).build()
        )


        Glide.with(context).load(glideUrl).placeholder(R.drawable.placeholder)
            .error(R.drawable.image_loading_error).override(1080).into(holder.thumbnail)

        holder.itemView.setOnClickListener {
            Toast.makeText(
                context,
                "Coming soon...",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}