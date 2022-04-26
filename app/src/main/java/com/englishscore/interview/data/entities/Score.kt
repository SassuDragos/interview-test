package com.englishscore.interview.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Represents a score object
 */
@Entity
data class Score(
    @PrimaryKey
    val id: Long = UUID.randomUUID().mostSignificantBits,
    val score: Long
)