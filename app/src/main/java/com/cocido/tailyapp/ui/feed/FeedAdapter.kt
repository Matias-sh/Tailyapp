package com.cocido.tailyapp.ui.feed

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cocido.tailyapp.R
import com.cocido.tailyapp.data.model.Post

class FeedAdapter(private val posts: List<Post>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    inner class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val user = view.findViewById<TextView>(R.id.tvUserName)
        val location = view.findViewById<TextView>(R.id.tvLocation)
        val tag = view.findViewById<TextView>(R.id.tvTag)
        val text = view.findViewById<TextView>(R.id.tvDescription)
        val image = view.findViewById<ImageView>(R.id.imgPost)
        val reactionSummary = view.findViewById<TextView>(R.id.tvReactionSummary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val post = posts[position]
        holder.user.text = post.user
        holder.location.text = post.location
        holder.tag.text = post.tag
        holder.text.text = post.text
        holder.reactionSummary.text = "a ${post.likes} personas les alegra este ${post.tag.lowercase()}"
        Glide.with(holder.image).load(post.imageUrl).into(holder.image)

        holder.image.setOnClickListener {
            val context = holder.itemView.context
            val dialog = Dialog(context)
            val imageView = ImageView(context)

            // Cargamos la imagen original
            Glide.with(context).load(post.imageUrl).into(imageView)

            dialog.setContentView(imageView)

            // El fondo transparente permite ver el fondo real
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Que se cierre al tocar afuera
            dialog.setCanceledOnTouchOutside(true)

            dialog.show()
        }
    }

    override fun getItemCount() = posts.size
}
