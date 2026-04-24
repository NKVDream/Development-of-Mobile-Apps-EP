// data/repository/WordCardRepository.kt
package com.example.ep2.data.repository

import com.example.ep2.data.model.WordCard
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class WordCardRepository {
    private val db = FirebaseFirestore.getInstance()
    private val cardsCollection = db.collection("wordCards")


    fun addCard(
        card: WordCard,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        cardsCollection.add(card)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getAllCards(): Flow<List<WordCard>> = callbackFlow {
        val listener = cardsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val cards = snapshot?.documents?.mapNotNull {
                it.toObject(WordCard::class.java)?.copy(id = it.id)
            } ?: emptyList()

            trySend(cards)
        }
        awaitClose { listener.remove() }
    }

    fun updateCard(cardId: String, word: String, translation: String) {
        val updates = mapOf(
            "word" to word,
            "translation" to translation
        )
        cardsCollection.document(cardId).update(updates)
    }

    fun deleteCard(cardId: String) {
        cardsCollection.document(cardId).delete()
    }
    fun updateCardStats(cardId: String, correctCount: Int? = null, wrongCount: Int? = null) {
        val updates = mutableMapOf<String, Any>()
        correctCount?.let { updates["correctCount"] = it }
        wrongCount?.let { updates["wrongCount"] = it }
        updates["lastReviewed"] = System.currentTimeMillis()

        if (updates.isNotEmpty()) {
            cardsCollection.document(cardId).update(updates)
        }
    }
}