package com.app.instrumentationtestexample.service

import com.app.instrumentationtestexample.model.Team

interface Service {

    fun getTeams(callback: (List<Team>?) -> Unit)
}