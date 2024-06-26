package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import okhttp3.MultipartBody
import java.io.File
import androidx.paging.liveData
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.response.StoryResponse
import com.example.storyapp.response.UploadStoryResponse


class StoryRepository {

    fun getPager(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(this, token) }
        ).liveData
    }

    suspend fun getStories(token: String, page: Int): StoryResponse {
        val apiServiceWithToken = ApiConfig.getApiService(token)
        return apiServiceWithToken.getStories(page)
    }

    suspend fun getStoriesWithLocation(token: String): StoryResponse {
        val apiServiceWithToken = ApiConfig.getApiService(token)
        return apiServiceWithToken.getStoriesWithLocation(1)
    }

    suspend fun uploadImage(imageFile: File, description: String, lat: Float?, lon: Float?, token: String
    ): UploadStoryResponse {
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val imageBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("photo", imageFile.name, imageBody)
        val latPart = lat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val lonPart = lon?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val apiServiceWithToken = ApiConfig.getApiService(token)
        return apiServiceWithToken.uploadImage(body, descriptionBody, latPart, lonPart)
    }
}