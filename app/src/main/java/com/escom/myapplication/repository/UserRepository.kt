package com.escom.myapplication.repository

import android.content.Context
import com.escom.myapplication.api.RetrofitClient
import com.escom.myapplication.model.User

class UserRepository(private val context: Context) {
    private val apiService = RetrofitClient.getApiService(context)
    
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val credentials = mapOf("email" to email, "password" to password)
            val response = apiService.login(credentials)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun register(email: String, password: String, firstName: String, lastName: String): Result<User> {
        return try {
            val userData = mapOf(
                "email" to email,
                "password" to password,
                "firstName" to firstName,
                "lastName" to lastName
            )
            val response = apiService.register(userData)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Add the missing methods for user management
    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val response = apiService.getAllUsers()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserRole(userId: Long, role: String): Result<String> {
        return try {
            val roleData = mapOf("role" to role)
            val response = apiService.updateUserRole(userId, roleData)
            if (response.isSuccessful) {
                Result.success("Rol actualizado correctamente")
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteUser(userId: Long): Result<String> {
        return try {
            val response = apiService.deleteUser(userId)
            if (response.isSuccessful) {
                Result.success("Usuario eliminado correctamente")
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
