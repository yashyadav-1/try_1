package com.yyn.task_2

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
//import retrofit2.converter.gson.GsonConverterFactory


interface ApiService {
    companion object {
        private const val BASE_URL =  "https://api.flickr.com/services/rest/?"
        private const val API_KEY = "6f102c62f41998d151e5a1b48713cf13"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

    @GET("?method=flickr.photos.getRecent")
    suspend fun getRecentPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("extras") extras: String = "url_s",
        @Query("api_key") apiKey: String = API_KEY
    ): Response<PhotoResponse>

    @GET("?method=flickr.photos.search")
    suspend fun searchPhotos(
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("extras") extras: String = "url_s",
        @Query("api_key") apiKey: String = API_KEY
    ): Response<PhotoResponse>
}

fun createApiService(): ApiService {
    return ApiService.create()
}
