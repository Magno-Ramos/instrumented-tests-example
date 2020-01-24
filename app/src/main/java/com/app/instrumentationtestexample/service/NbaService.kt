package com.app.instrumentationtestexample.service

import com.app.instrumentationtestexample.BuildConfig
import com.app.instrumentationtestexample.model.Team
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NbaService : Service {

    private var service: RetrofitService

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        service = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RetrofitService::class.java)
    }

    override fun getTeams(callback: (List<Team>?) -> Unit) {
        service.getTeams().enqueue(object : Callback<List<Team>> {
            override fun onResponse(call: Call<List<Team>>, response: Response<List<Team>>) {
                if (response.isSuccessful) {
                    callback.invoke(response.body())
                }
            }

            override fun onFailure(call: Call<List<Team>>, t: Throwable) {
                callback.invoke(null)
            }
        })
    }
}