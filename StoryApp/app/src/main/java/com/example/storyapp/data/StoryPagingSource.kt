package com.example.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.response.ListStoryItem

class StoryPagingSource(
    private val storyRepository: StoryRepository,
    private val token: String
) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = storyRepository.getStories(token, nextPageNumber)
            val stories = response.listStory.orEmpty().filterNotNull()

            LoadResult.Page(
                data = stories,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (stories.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
