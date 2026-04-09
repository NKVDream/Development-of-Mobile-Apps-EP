package com.example.dma_ep_4

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordCardDao {

    @Insert
    suspend fun insert(card: WordCard)

    @Update
    suspend fun update(card: WordCard)

    @Delete
    suspend fun delete(card: WordCard)

    @Query("SELECT * FROM word_cards ORDER BY id ASC")
    fun getAllCards(): LiveData<List<WordCard>>

    @Query("SELECT * FROM word_cards WHERE isLearned = 0 ORDER BY lastReviewed ASC")
    fun getUnlearnedCards(): LiveData<List<WordCard>>

    @Query("SELECT COUNT(*) FROM word_cards")
    fun getTotalCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM word_cards WHERE isLearned = 1")
    fun getLearnedCount(): LiveData<Int>

    @Query("UPDATE word_cards SET isLearned = 1 WHERE id = :cardId")
    suspend fun markAsLearned(cardId: Int)

    @Query("UPDATE word_cards SET correctAnswers = correctAnswers + 1, lastReviewed = :currentTime WHERE id = :cardId")
    suspend fun incrementCorrectAnswers(cardId: Int, currentTime: Long)

    @Query("UPDATE word_cards SET wrongAnswers = wrongAnswers + 1, lastReviewed = :currentTime WHERE id = :cardId")
    suspend fun incrementWrongAnswers(cardId: Int, currentTime: Long)
}