package com.example.ep2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.ep2.MainActivity
import com.example.ep2.R
import com.example.ep2.ui.adapters.CardStackAdapter
import com.example.ep2.ui.viewmodel.WordCardViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.abs

class CardStackFragment : Fragment() {

    private lateinit var viewModel: WordCardViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var fabAddCard: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_card_stack, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(
            requireContext(),
            "Загружены карточки",
            Toast.LENGTH_SHORT
        ).show()

        viewModel = ViewModelProvider(this)[WordCardViewModel::class.java]
        viewPager = view.findViewById(R.id.viewPager)
        fabAddCard = view.findViewById(R.id.fabAddCard)

        setupViewPager()
        observeData()

        fabAddCard.setOnClickListener {
            showAddCardDialog()
        }
    }

    private fun setupViewPager() {
        viewPager.apply {
            offscreenPageLimit = 3
            setPageTransformer { page: View, position: Float ->
                page.apply {
                    val scaleFactor = 0.95f
                    scaleX = 1f - (scaleFactor * abs(position))
                    scaleY = 1f - (scaleFactor * abs(position))
                    translationY = -30f * position
                    alpha = 1f - (0.5f * abs(position))
                }
            }
        }
    }

    private fun observeData() {
        viewModel.allCards.observe(viewLifecycleOwner) { cards ->
            if (cards.isNotEmpty()) {
                val adapter = CardStackAdapter(this, cards)
                viewPager.adapter = adapter
            }
        }
    }

    private fun showAddCardDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_card, null)

        val etWord = dialogView.findViewById<EditText>(R.id.etWord)
        val etTranslation = dialogView.findViewById<EditText>(R.id.etTranslation)

        AlertDialog.Builder(requireContext())
            .setTitle("Добавить новое слово")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val word = etWord.text.toString().trim()
                val translation = etTranslation.text.toString().trim()

                if (word.isNotEmpty() && translation.isNotEmpty()) {
                    viewModel.addCard(word, translation)

                    Toast.makeText(
                        requireContext(),
                        "Карточка \"$word\" добавлена!",
                        Toast.LENGTH_SHORT
                    ).show()

                    (activity as? MainActivity)?.showReminderNotification()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Заполните оба поля!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}