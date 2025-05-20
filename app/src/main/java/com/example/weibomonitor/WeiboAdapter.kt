package com.example.weibomonitor

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class WeiboAdapter(private val posts: List<WeiboPost>) : RecyclerView.Adapter<WeiboAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageAvatar: ImageView = view.findViewById(R.id.imageAvatar)
        val textName: TextView = view.findViewById(R.id.textName)
        val textContent: TextView = view.findViewById(R.id.textContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weibo_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.textName.text = post.userName
        holder.textContent.text = post.content
        Picasso.get().load(post.userAvatar).into(holder.imageAvatar)
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.url))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = posts.size
} 