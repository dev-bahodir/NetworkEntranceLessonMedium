package dev.bahodir.networkentrancelessonmedium.retrofit

import dev.bahodir.networkentrancelessonmedium.user.User
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("arkhiv-kursov-valyut/json/")
    fun getUsers(): Call<List<User>>
}