package com.dicoding.mypemudaapp

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro {
    fun getRetroClientInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("http://34.101.101.213:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}