package com.example.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.UserRepository
import com.example.storyapp.di.Injection
import com.example.storyapp.maps.MapsViewModel
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.main.MainViewModel
import com.example.storyapp.ui.signup.SignupViewModel
import com.example.storyapp.ui.uploadstory.UploadStoryViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository,storyRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(UploadStoryViewModel::class.java) -> {
                UploadStoryViewModel(userRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(userRepository, storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val userRepository = Injection.provideUserRepository(context)
                    val storyRepository = Injection.provideStoryRepository()
                    INSTANCE = ViewModelFactory(userRepository, storyRepository)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
