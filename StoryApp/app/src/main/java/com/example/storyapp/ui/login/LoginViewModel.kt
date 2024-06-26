package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginErrorMessage = MutableLiveData<String>()
    val loginErrorMessage: LiveData<String> = _loginErrorMessage

    private val _loginResultLiveData = MutableLiveData<LoginResponse>()
    val loginResultLiveData: LiveData<LoginResponse> = _loginResultLiveData

    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    fun loginUser(email: String, password: String) {
        _progressBarVisibility.value = true
        viewModelScope.launch {
            try {
                val (response, loginResult) = repository.loginUser(email, password)
                if (!response.error!! && loginResult != null) {
                    _loginResultLiveData.value = response
                    val userModel = loginResult.token?.let { UserModel(email, it) }
                    userModel?.let { saveSession(it) }
                } else {
                    _loginErrorMessage.value = "Login failed: ${response.message}"
                }
            } catch (e: Exception) {
                _loginErrorMessage.value = "Login failed: ${e.message}"
            } finally {
                _progressBarVisibility.value = false
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}