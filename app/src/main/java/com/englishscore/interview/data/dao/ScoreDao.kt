package com.englishscore.interview.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.englishscore.interview.data.entities.Score
import kotlinx.coroutines.flow.Flow


@Dao
interface ScoreDao {
    @Query("SELECT * FROM score ")
    fun getScores(): Flow<List<Score>>

    @Query("SELECT count(*) FROM score")
    fun getCount(): Int

    @Query("DELETE FROM score")
    fun clearScores()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scores: Score)
}