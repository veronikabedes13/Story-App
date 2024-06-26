package com.example.storyapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.UserRepository
import com.example.storyapp.response.ListStoryItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class MapsViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _storiesWithLocation = MutableLiveData<List<ListStoryItem>>()
    val storiesWithLocation: LiveData<List<ListStoryItem>> get() = _storiesWithLocation

    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    fun getStoriesWithLocation() {
        _progressBarVisibility.value = true
        viewModelScope.launch {
            try {
                val userSession = userRepository.getSession().first()
                val response = storyRepository.getStoriesWithLocation(userSession!!.token)
                if (response.error == true) {
                    _errorMessage.value = response.message
                } else {
                    _storiesWithLocation.value = response.listStory.orEmpty().filterNotNull()
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                _progressBarVisibility.value = false
            }
        }
    }
}