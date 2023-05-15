package com.nile.fortu.game

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nile.fortu.game.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding
        get() = _binding ?: throw RuntimeException("MenuFragment == null")

    private lateinit var onReturnButtonPressed: OnReturnButtonPressed

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnReturnButtonPressed) {
            onReturnButtonPressed = context
        } else {
            throw RuntimeException("MenuActivity must implement OnReturnButtonPressed interface")
        }
    }

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
            val fragment = ChooseGameFragment.newInstance()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.menu_activity_fragment_container, fragment)
                .commit()
        }

        binding.flSettings.setOnClickListener {
            val fragment = SettingsFragment()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.menu_activity_fragment_container, fragment)
                .commit()
        }

        binding.flPrivacy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
            startActivity(browserIntent)
        }

        binding.btnReturn.setOnClickListener {
            onReturnButtonPressed.onReturnButtonPressed()
        }

        binding.btnLogout.setOnClickListener {
            val intent = GoActivity.newIntent(requireContext())
            startActivity(intent)
        }
    }

    interface OnReturnButtonPressed {
        fun onReturnButtonPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}