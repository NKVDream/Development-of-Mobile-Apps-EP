package com.example.dma_ep_4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class CardsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewEmpty: TextView
    private lateinit var buttonAdd: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvCards)
        textViewEmpty = view.findViewById(R.id.tvEmpty)
        buttonAdd = view.findViewById(R.id.btnAddCard)

        val database = AppDatabase.getDatabase(requireContext())
        val cardDao = database.wordCardDao()

        buttonAdd.setOnClickListener {
            addNewCard(cardDao)
        }

        showCardsList(cardDao)
    }


    private fun addNewCard(cardDao: WordCardDao) {
        lifecycleScope.launch {
            val newCard = WordCard(
                word = "Слово ${System.currentTimeMillis()}",
                translation = "перевод слова",
                correctAnswers = 0,
                wrongAnswers = 0,
                lastReviewed = System.currentTimeMillis(),
                isLearned = false
            )

            cardDao.insert(newCard)

            Toast.makeText(requireContext(), "Карточка добавлена", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showCardsList(cardDao: WordCardDao) {
        cardDao.getAllCards().observe(viewLifecycleOwner) { cards ->
            if (cards.isEmpty()) {
                textViewEmpty.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                textViewEmpty.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                val adapter = WordCardAdapter(cards) { card ->
                    deleteCard(cardDao, card)
                }

                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun deleteCard(cardDao: WordCardDao, card: WordCard) {
        lifecycleScope.launch {
            cardDao.delete(card)
            Toast.makeText(requireContext(), "Карточка удалена", Toast.LENGTH_SHORT).show()
        }
    }
}