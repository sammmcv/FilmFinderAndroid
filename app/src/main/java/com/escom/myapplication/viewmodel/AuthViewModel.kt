package com.escom.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escom.myapplication.model.User
import com.escom.myapplication.repository.UserRepository
import com.escom.myapplication.utils.SessionManager
import kotlinx.coroutines.launch

class AuthViewModel(private val context: Context) : ViewModel() {
    private val userRepository = UserRepository(context)
    private val sessionManager = SessionManager(context)
    
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    
    private val _operationMessage = MutableLiveData<String?>()
    val operationMessage: LiveData<String?> = _operationMessage

    init {
        // Verificar si hay una sesión guardada al iniciar el ViewModel
        val savedUser = sessionManager.getUserSession()
        if (savedUser != null && sessionManager.isLoggedIn()) {
            _user.value = savedUser
        }
    }
    
    fun login(email: String, password: String) {
        _loading.value = true
        _error.value = null
        
        viewModelScope.launch {
            val result = userRepository.login(email, password)
            _loading.value = false
            
            result.fold(
                onSuccess = { 
                    _user.value = it
                    sessionManager.saveUserSession(it)
                },
                onFailure = { _error.value = it.message }
            )
        }
    }
    
    fun register(email: String, password: String, firstName: String, lastName: String) {
        _loading.value = true
        _error.value = null
        
        viewModelScope.launch {
            val result = userRepository.register(email, password, firstName, lastName)
            _loading.value = false
            
            result.fold(
                onSuccess = { 
                    _user.value = it
                    sessionManager.saveUserSession(it)
                },
                onFailure = { _error.value = it.message }
            )
        }
    }
    
    fun logout() {
        sessionManager.clearSession()
        _user.value = null
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun clearOperationMessage() {
        _operationMessage.value = null
    }
    
    fun getAllUsers() {
        _loading.value = true
        _error.value = null
        
        viewModelScope.launch {
            val result = userRepository.getAllUsers()
            _loading.value = false
            
            result.fold(
                onSuccess = { _users.value = it },
                onFailure = { _error.value = it.message }
            )
        }
    }
    
    fun updateUserRole(userId: Long, role: String) {
        _loading.value = true
        _error.value = null
        _operationMessage.value = null
        
        viewModelScope.launch {
            val result = userRepository.updateUserRole(userId, role)
            _loading.value = false
            
            result.fold(
                onSuccess = { 
                    _operationMessage.value = it
                    getAllUsers() // Refresh the list
                },
                onFailure = { _error.value = it.message }
            )
        }
    }
    
    fun deleteUser(userId: Long) {
        _loading.value = true
        _error.value = null
        _operationMessage.value = null
        
        viewModelScope.launch {
            val result = userRepository.deleteUser(userId)
            _loading.value = false
            
            result.fold(
                onSuccess = { 
                    _operationMessage.value = it
                    getAllUsers() // Refresh the list
                },
                onFailure = { _error.value = it.message }
            )
        }
    }
}
