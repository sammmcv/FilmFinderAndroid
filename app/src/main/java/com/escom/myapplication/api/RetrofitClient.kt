package com.escom.myapplication.api

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    // Interceptor personalizado para no registrar información sensible
    class NoSensitiveDataInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            Log.d("HTTP_REQUEST", "URL: ${request.url}")
            Log.d("HTTP_REQUEST", "Method: ${request.method}")
            // No registrar el cuerpo de la solicitud
            return chain.proceed(request)
        }
    }

    private fun getClient(context: Context): Retrofit {
        if (retrofit == null) {
            // Crear interceptor de logging para depuración
            val logging = HttpLoggingInterceptor { message ->
                // No registrar el cuerpo de la solicitud
                if (!message.contains("password") && !message.contains("token")) {
                    Log.d("HTTP_LOG", message)
                }
            }
            logging.level = HttpLoggingInterceptor.Level.BODY

            // Crear cliente OkHttp con el interceptor personalizado y el de logging
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(NoSensitiveDataInterceptor())
                .addInterceptor(logging)
                .build()

            // Use a hardcoded base URL instead of accessing it from resources
            // This avoids the need for the R class reference
            val baseUrl = "http://192.168.20.67:8080" // For emulator pointing to localhost
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