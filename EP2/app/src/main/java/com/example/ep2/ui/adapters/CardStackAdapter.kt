package com.example.ep2.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ep2.data.model.WordCard
import com.example.ep2.ui.fragments.CardItemFragment

class CardStackAdapter(
    fragment: Fragment,
    private val cards: List<WordCard>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = cards.size

    override fun createFragment(position: Int): Fragment {
        return CardItemFragment.newInstance(cards[position])
    }
}