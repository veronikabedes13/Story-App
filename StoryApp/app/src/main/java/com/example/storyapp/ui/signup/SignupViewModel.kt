package com.example.storyapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.example.storyapp.response.ErrorResponse
import com.example.storyapp.response.RegisterResponse
import com.example.storyapp.data.UserRepository


class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registrationStatus = MutableLiveData<String>()
    val registrationStatus: LiveData<String>
        get() = _registrationStatus

    private val _registerResultLiveData = MutableLiveData<RegisterResponse>()
    val registerResultLiveData: LiveData<RegisterResponse> = _registerResultLiveData

    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    fun registerUser(name: String, email: String, password: String) {
        _progressBarVisibility.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.registerUser(name, email, password)
                if (response.error == false) {
                    _registrationStatus.value = "Registration successful"
                    _registerResultLiveData.value = response
                } else {
                    _registrationStatus.value = "Registration failed: ${response.message}"
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message ?: "Unknown error"
                _registrationStatus.value = "Registration failed: $errorMessage"
            } catch (e: Exception) {
                _registrationStatus.value = "Registration failed: ${e.message}"
            } finally {
                _progressBarVisibility.value = false
            }
        }
    }
}

