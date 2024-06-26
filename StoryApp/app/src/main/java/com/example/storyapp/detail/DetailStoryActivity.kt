package com.example.storyapp.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.response.ListStoryItem

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyItem = intent.getParcelableExtra<ListStoryItem>("LIST_STORY_ITEM")

        storyItem?.let {
            binding.nameTextView.text = it.name
            binding.descriptionTextView.text = it.description

            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.imageView)
        }
    }
}