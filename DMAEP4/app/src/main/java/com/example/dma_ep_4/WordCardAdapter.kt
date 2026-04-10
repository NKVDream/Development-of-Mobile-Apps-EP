package com.example.dma_ep_4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordCardAdapter(
    private val cards: List<WordCard>,
    private val onCardClick: (WordCard) -> Unit
) : RecyclerView.Adapter<WordCardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWord: TextView = itemView.findViewById(R.id.tvWord)
        val tvTranslation: TextView = itemView.findViewById(R.id.tvTranslation)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]

        holder.tvWord.text = card.word
        holder.tvTranslation.text = card.translation

        holder.tvStatus.text = if (card.isLearned) {
            "Выучено (правильных: ${card.correctAnswers})"
        } else {
            "В процессе (правильных: ${card.correctAnswers})"
        }

        holder.itemView.setOnClickListener {
            onCardClick(card)
        }
    }

    override fun getItemCount(): Int = cards.size
}