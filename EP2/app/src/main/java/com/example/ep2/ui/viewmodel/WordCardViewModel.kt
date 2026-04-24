package com.example.ep2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ep2.data.model.WordCard
import com.example.ep2.data.repository.WordCardRepository
import kotlinx.coroutines.launch

class WordCardViewModel : ViewModel() {
    private val repository = WordCardRepository()

    val allCards: LiveData<List<WordCard>> = repository.getAllCards().asLiveData()

    fun addCard(word: String, translation: String) {
        val card = WordCard(
            word = word,
            translation = translation
        )
        viewModelScope.launch {
            repository.addCard(
                card,
                onSuccess = { /* Успешно */ },
                onFailure = { /* Ошибка */ }
            )
        }
    }

    fun updateCard(cardId: String, word: String, translation: String) {
        viewModelScope.launch {
            repository.updateCard(cardId, word, translation)
        }
    }

    fun deleteCard(cardId: String) {
        viewModelScope.launch {
            repository.deleteCard(cardId)
        }
    }

    fun markAsCorrect(card: WordCard) {
        viewModelScope.launch {
            repository.updateCardStats(
                card.id,
                correctCount = card.correctCount + 1
            )
        }
    }

    fun markAsWrong(card: WordCard) {
        viewModelScope.launch {
            repository.updateCardStats(
                card.id,
                wrongCount = card.wrongCount + 1
            )
        }
    }
}