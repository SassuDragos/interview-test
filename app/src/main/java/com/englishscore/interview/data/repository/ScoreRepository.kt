package com.englishscore.interview.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.englishscore.interview.data.dao.ScoreDao
import com.englishscore.interview.network.NetworkService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class ScoreRepository @Inject constructor(
    private val webservice: NetworkService,
    private val scoreDao: ScoreDao
) {
    fun getScores() = scoreDao.getScores()

    fun updateLatestScores() {
        GlobalScope.launch {
            try {
                val response = webservice.getScores()
                response.body()?.let {
                    scoreDao.insert(it)
                }
            } catch (ex: Exception) {
                Log.e("ERROR", ex.localizedMessage)
            }
        }
    }
}

