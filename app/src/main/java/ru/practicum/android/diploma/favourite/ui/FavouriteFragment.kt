package ru.practicum.android.diploma.favourite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.databinding.FavouriteFragmentBinding

class FavouriteFragment : Fragment() {
    private var _binding: FavouriteFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FavouriteFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}
