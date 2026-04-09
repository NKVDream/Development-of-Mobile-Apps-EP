package com.example.dma_ep_4

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_cards")
data class WordCard(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String,
    val translation: String,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val lastReviewed: Long = 0,
    val isLearned: Boolean = false
)