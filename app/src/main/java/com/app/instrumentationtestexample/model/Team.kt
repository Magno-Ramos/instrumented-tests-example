package com.app.instrumentationtestexample.model

import java.io.Serializable

data class Team(
    val teamId: Int,
    val teamName: String,
    val location: String,
    val image: String
) : Serializable