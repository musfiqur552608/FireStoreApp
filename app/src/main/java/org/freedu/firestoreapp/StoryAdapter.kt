package org.freedu.firestoreapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class StoryAdapter(private val story:List<Story>, private val itemClickListener: ItemClickListener):
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.titleTxt)
        val desc = itemView.findViewById<TextView>(R.id.descTxt)
        val edit = itemView.findViewById<ImageButton>(R.id.editBtn)
        val delete = itemView.findViewById<ImageButton>(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return story.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = story[position]
        holder.title.text = story.title
        holder.desc.text = story.description

        holder.edit.setOnClickListener {
            itemClickListener.onEditItemClick(story)
        }
        holder.delete.setOnClickListener {
            itemClickListener.onDeleteItemClick(story)
        }

    }
}

interface ItemClickListener {
    fun onEditItemClick(story:Story)
    fun onDeleteItemClick(story:Story)
}
