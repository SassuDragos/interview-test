package com.englishscore.interview.network

import com.englishscore.interview.data.entities.Score
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET("tech-test/score.json")
    suspend fun getScores(): Response<Score>
}