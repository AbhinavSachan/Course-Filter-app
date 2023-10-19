package com.abhinavdev.taskapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhinavdev.taskapp.R
import com.abhinavdev.taskapp.models.Index
import com.abhinavdev.taskapp.models.Result
import com.abhinavdev.taskapp.ui.SecondActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CollectionAdaptor(val result: Result?, val context: Context) :
    RecyclerView.Adapter<CollectionAdaptor.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val collectionName: TextView = view.findViewById(R.id.collection_name)
        val descriptionBtn: ImageView = view.findViewById(R.id.description_btn)
        val viewAllBtn: TextView = view.findViewById(R.id.view_all_btn)
        val sorryText: TextView = view.findViewById(R.id.sorry_text)
        val contentRecyclerView: RecyclerView = view.findViewById(R.id.content_rec_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.collection_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return result?.collections?.smart?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val smart = result?.collections?.smart?.get(position)
        holder.collectionName.text = smart?.label
        holder.descriptionBtn.setOnClickListener {
            showDescriptionDialog(smart?.description)
        }
        holder.viewAllBtn.setOnClickListener {
            if (smart?.courses != null && smart.courses?.isNotEmpty() == true) {
                context.startActivity(Intent(context, SecondActivity::class.java).apply {
                    putExtra(SecondActivity.EXTRA_COLLECTION_ID, smart)
                })
            }else{
                Toast.makeText(context,"No Data", Toast.LENGTH_SHORT).show()
            }
        }
        val videoListToDisplay: MutableList<Index?> = mutableListOf()
        // since i know that courses are not null so i m using this (!!) otherwise its not recommended
        for (course in smart?.courses!!) {
            for (index in result?.index!!) {
                if (index?.id == course) {
                    videoListToDisplay.add(index)
                }
            }
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.contentRecyclerView.layoutManager = layoutManager
        holder.contentRecyclerView.setHasFixedSize(true)
        if (videoListToDisplay.isNotEmpty()) {
            holder.contentRecyclerView.adapter = VideoAdaptor(videoListToDisplay, context)
            holder.sorryText.visibility = View.GONE
        } else {
            holder.sorryText.visibility = View.VISIBLE
        }
    }

    private fun showDescriptionDialog(description: String?) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle("Collection description")
        builder.setMessage(description ?: "Description Not Available.")
        builder.setPositiveButton("Ok", null)
        builder.create().show()
    }
}