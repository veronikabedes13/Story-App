package com.example.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.response.ListStoryItem
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.firstOrNull

class MainViewModel(
    private val repository: UserRepository,
    private val storyRepository: StoryRepository) : ViewModel() {

    private var currentStories: LiveData<PagingData<ListStoryItem>>? = null

    fun getSession(): LiveData<UserModel?> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }


    suspend fun getStories(): LiveData<PagingData<ListStoryItem>> {
        val userModel = repository.getSession().firstOrNull()

        return if (userModel != null) {
            val token = userModel.token
            val newStories = storyRepository.getPager(token)
                .cachedIn(viewModelScope)
            currentStories = newStories
            newStories
        } else {
            MutableLiveData()
        }
    }
}