package com.example.storyapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ItemRowBinding
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.detail.DetailStoryActivity


@Suppress("DEPRECATION")
class MainAdapter(private val context: Context) :
    PagingDataAdapter<ListStoryItem, MainAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class ViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(context, DetailStoryActivity::class.java)
                    intent.putExtra("LIST_STORY_ITEM", getItem(position) as Parcelable)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(item: ListStoryItem) {
            binding.apply {
                name.text = item.name
                description.text = item.description
                Glide.with(itemView)
                    .load(item.photoUrl)
                    .into(imageView)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}