package com.example.storyapp.ui

import com.example.storyapp.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "CreatedAt + $i",
                "name $i",
            )
            items.add(story)
        }
        return items
    }
}