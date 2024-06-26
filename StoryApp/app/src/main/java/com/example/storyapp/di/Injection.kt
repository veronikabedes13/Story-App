package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.api.ApiConfig

object Injection {
    private var userRepository: UserRepository? = null
    private var storyRepository: StoryRepository? = null

    fun provideUserRepository(context: Context): UserRepository {
        if (userRepository == null) {
            userRepository = UserRepository.getInstance(UserPreference.getInstance(context.dataStore), ApiConfig.getApiService(""))
        }
        return userRepository!!
    }

    fun provideStoryRepository(): StoryRepository {
        if (storyRepository == null) {
            storyRepository = StoryRepository()
        }
        return storyRepository!!
    }
}

