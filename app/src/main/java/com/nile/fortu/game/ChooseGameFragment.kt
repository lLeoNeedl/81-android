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
        get() = _binding ?: throw RuntimeException("ChooseGameFragment == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnGame1.setOnClickListener {
                val intent = FirstGameActivity.newIntent(requireContext())
                startActivity(intent)
            }

            btnGame2.setOnClickListener {
                val intent = SecondGameActivity.newIntent(requireContext())
                startActivity(intent)
            }

            binding.tvReturnToMenu.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            binding.btnBonusGame.setOnClickListener {
                val intent = BonusGameActivity.newIntent(requireContext())
                startActivity(intent)
            }

            binding.btnLogout.setOnClickListener {
                val intent = GoActivity.newIntent(requireContext())
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ChooseGameFragment()
    }
}