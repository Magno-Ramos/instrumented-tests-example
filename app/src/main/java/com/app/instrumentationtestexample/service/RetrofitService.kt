package com.app.instrumentationtestexample.service

import com.app.instrumentationtestexample.model.Team
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    @GET("teams")
    fun getTeams(): Call<List<Team>>
}