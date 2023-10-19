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
import com.abhinavdev.taskapp.models.Index
import com.abhinavdev.taskapp.utils.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

class VideoAdaptor(val list: MutableList<Index?>, val context: Context) :
    RecyclerView.Adapter<VideoAdaptor.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.title_tv)
        val educatorText: TextView = view.findViewById(R.id.educator_tv)
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail_img)
        val ownedTxt: View = view.findViewById(R.id.owned_text_rl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.video_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = list[position]
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