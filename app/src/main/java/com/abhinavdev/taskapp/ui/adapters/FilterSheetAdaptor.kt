package com.abhinavdev.taskapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abhinavdev.taskapp.R
import com.abhinavdev.taskapp.models.FilterModel
import com.abhinavdev.taskapp.ui.SecondActivity
import com.abhinavdev.taskapp.utils.CourseFilter

class FilterSheetAdaptor(val list: MutableList<FilterModel?>?, val context: Context) :
    RecyclerView.Adapter<FilterSheetAdaptor.ViewHolder>() {

    private var courseFilter: CourseFilter? = null

    fun getItem(position: Int): FilterModel? {
        return list?.get(position)
    }

    fun setFilterType(type: CourseFilter) {
        courseFilter = type
    }

    private fun getFilterType(): CourseFilter? {
        return courseFilter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.filter_layout_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = list?.get(position)
        holder.filterName.text = filter?.filterName
        if (filter?.isApplied == true) {
            holder.tickMark.visibility = View.VISIBLE
        } else {
            holder.tickMark.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            (context as SecondActivity).hideSheet()
            if (getFilterType() == CourseFilter.SHOW_OWNED && filter?.filterName == "Yes") {
                context.applyOwnedFilter()
            } else if (getFilterType() == CourseFilter.SHOW_OWNED && filter?.filterName == "No") {
                context.clearOwnedFilter()
            } else if (getFilterType() == CourseFilter.EDUCATOR) {
                context.applyEducatorFilter(filter?.filterName)
            } else {
                context.applyFilter(filter, getFilterType())
            }
            context.setSelected(getFilterType(),true)
            context.setSelectedTitle(getFilterType(),filter?.filterName)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filterName: TextView = itemView.findViewById(R.id.filter_name)
        val tickMark: ImageView = itemView.findViewById(R.id.tick_mark)
    }
}
