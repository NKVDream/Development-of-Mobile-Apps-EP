package com.example.ep2.data.model

data class WordCard(
    val id: String = "",
    val word: String = "",
    val translation: String = "",
    val deckId: String = "default",
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val lastReviewed: Long = 0
)