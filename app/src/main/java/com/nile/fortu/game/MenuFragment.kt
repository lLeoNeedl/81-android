package com.nile.fortu.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nile.fortu.game.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding
        get() = _binding ?: throw RuntimeException("FragmentMenuBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.flGames.setOnClickListener {
            val fragment = ChooseGameFragment()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .addToBackStack("choose_game")
                .replace(R.id.menu_activity_fragment_container, fragment, "choose_game")
                .commit()
        }

        binding.flSettings.setOnClickListener {
            val fragment = SettingsFragment()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .addToBackStack("settings")
                .replace(R.id.menu_activity_fragment_container, fragment, "settings")
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
    }
}