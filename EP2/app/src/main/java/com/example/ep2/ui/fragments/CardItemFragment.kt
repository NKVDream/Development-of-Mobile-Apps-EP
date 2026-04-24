package com.example.ep2.ui.fragments

import com.google.android.material.snackbar.Snackbar
import android.widget.Toast
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ep2.R
import com.example.ep2.data.model.WordCard
import com.example.ep2.ui.viewmodel.WordCardViewModel

class CardItemFragment : Fragment() {

    companion object {
        private const val ARG_CARD_ID = "card_id"
        private const val ARG_WORD = "word"
        private const val ARG_TRANSLATION = "translation"

        fun newInstance(card: WordCard): CardItemFragment {
            return CardItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CARD_ID, card.id)
                    putString(ARG_WORD, card.word)
                    putString(ARG_TRANSLATION, card.translation)
                }
            }
        }
    }

    private var isFlipped = false
    private lateinit var cardId: String
    private lateinit var word: String
    private lateinit var translation: String
    private lateinit var viewModel: WordCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cardId = it.getString(ARG_CARD_ID) ?: ""
            word = it.getString(ARG_WORD) ?: ""
            translation = it.getString(ARG_TRANSLATION) ?: ""
        }
        viewModel = ViewModelProvider(requireActivity())[WordCardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_card_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val frontLayout = view.findViewById<LinearLayout>(R.id.frontLayout)
        val backLayout = view.findViewById<LinearLayout>(R.id.backLayout)
        val wordTextView = view.findViewById<TextView>(R.id.wordTextView)
        val translationTextView = view.findViewById<TextView>(R.id.translationTextView)
        val wordBackTextView = view.findViewById<TextView>(R.id.wordBackTextView)

        val btnEdit = view.findViewById<ImageButton>(R.id.btnEdit)
        val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)

        val btnEditBack = view.findViewById<ImageButton>(R.id.btnEditBack)
        val btnDeleteBack = view.findViewById<ImageButton>(R.id.btnDeleteBack)

        wordTextView.text = word
        translationTextView.text = translation
        wordBackTextView.text = word

        val editClickListener = View.OnClickListener { showEditDialog() }
        val deleteClickListener = View.OnClickListener { showDeleteConfirmation() }

        btnEdit.setOnClickListener(editClickListener)
        btnDelete.setOnClickListener(deleteClickListener)
        btnEditBack.setOnClickListener(editClickListener)
        btnDeleteBack.setOnClickListener(deleteClickListener)

        view.setOnClickListener {
            flipCard(frontLayout, backLayout)
        }
    }

    private fun flipCard(front: View, back: View) {
        val flipIn = android.view.animation.AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        val flipOut = android.view.animation.AnimationUtils.loadAnimation(context, android.R.anim.fade_out)

        if (isFlipped) {
            front.startAnimation(flipIn)
            back.startAnimation(flipOut)
            front.visibility = View.VISIBLE
            back.visibility = View.GONE
        } else {
            front.startAnimation(flipOut)
            back.startAnimation(flipIn)
            front.visibility = View.GONE
            back.visibility = View.VISIBLE
        }
        isFlipped = !isFlipped
    }

    private fun showEditDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_edit_card, null)

        val etWord = dialogView.findViewById<EditText>(R.id.etEditWord)
        val etTranslation = dialogView.findViewById<EditText>(R.id.etEditTranslation)

        etWord.setText(word)
        etTranslation.setText(translation)

        AlertDialog.Builder(requireContext())
            .setTitle("Редактировать карточку")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newWord = etWord.text.toString().trim()
                val newTranslation = etTranslation.text.toString().trim()

                if (newWord.isNotEmpty() && newTranslation.isNotEmpty()) {
                    viewModel.updateCard(cardId, newWord, newTranslation)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showDeleteConfirmation() {
        val snackbar = Snackbar.make(
            requireView(),
            "Удалить карточку \"$word\"?",
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction("Удалить") {
            viewModel.deleteCard(cardId)

            Toast.makeText(
                requireContext(),
                "Карточка \"$word\" удалена",
                Toast.LENGTH_SHORT
            ).show()
        }

        snackbar.setActionTextColor(android.graphics.Color.RED)
        snackbar.show()
    }
}