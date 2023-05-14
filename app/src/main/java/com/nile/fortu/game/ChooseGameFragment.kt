package com.nile.fortu.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nile.fortu.game.databinding.FragmentChooseGameBinding
import com.nile.fortu.game.databinding.FragmentMenuBinding

class ChooseGameFragment : Fragment() {

    private var _binding: FragmentChooseGameBinding? = null
    private val binding: FragmentChooseGameBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseGameBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseGameBinding.inflate(inflater, container, false)
        return binding.root
    }
}