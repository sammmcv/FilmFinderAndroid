package com.escom.myapplication.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// Remove the R import that's causing issues

object RetrofitClient {
    private var retrofit: Retrofit? = null
    
    private fun getClient(context: Context): Retrofit {
        if (retrofit == null) {
            // Crear interceptor de logging para depuración
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            
            // Crear cliente OkHttp con el interceptor
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            
            // Use a hardcoded base URL instead of accessing it from resources
            // This avoids the need for the R class reference
            val baseUrl = "http://192.168.100.112:8080" // For emulator pointing to localhost
            // If you're testing on a real device, you might need to use your computer's IP address
            
            // Crear instancia de Retrofit
            retrofit = Retrofit.Builder()
                .baseUrl("$baseUrl/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        }
        return retrofit!!
    }
    
    fun getApiService(context: Context): ApiService {
        return getClient(context).create(ApiService::class.java)
    }
}