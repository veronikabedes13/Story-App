package com.example.storyapp.ui.uploadstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import com.example.storyapp.data.UserRepository

class UploadStoryViewModel (
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> get() = _navigateToMain

    private val _includeLocation = MutableLiveData<Boolean>()

    private var userLat: Float? = null
    private var userLon: Float? = null

    fun setIncludeLocation(includeLocation: Boolean) {
        _includeLocation.value = includeLocation
    }

    fun setUserLocation(lat: Float, lon: Float) {
        userLat = lat
        userLon = lon
    }

    fun uploadImage(imageFile: File, description: String) {
        viewModelScope.launch {
            try {
                _progressBarVisibility.value = true
                val lat = if (_includeLocation.value == true) userLat else null
                val lon = if (_includeLocation.value == true) userLon else null
                val userSession = userRepository.getSession().first()
                val token = userSession!!.token
                storyRepository.uploadImage(imageFile, description, lat, lon, token)
                _errorMessage.value = "Image uploaded successfully"
                _navigateToMain.value = true
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred while uploading image: ${e.message}"
            } finally {
                _progressBarVisibility.value = false
            }
        }
    }
}
