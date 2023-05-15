package com.nile.fortu.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nile.fortu.game.databinding.FragmentAuthorizationBinding
import com.nile.fortu.game.databinding.FragmentEnterPhoneNumberBinding


class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding: FragmentAuthorizationBinding
        get() = _binding ?: throw RuntimeException("AuthorizationFragment == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPhone.setOnClickListener {
            binding.rbPhone.isChecked = true
        }

        binding.tvAnonymous.setOnClickListener {
            binding.rbAnonymous.isChecked = true
        }

        binding.flContinue.setOnClickListener {
            if (binding.rbPhone.isChecked) {
                val fragment = EnterPhoneNumberFragment.newInstance()
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.main_activity_fragment_container, fragment)
                    .commit()
            } else {
                val intent = MenuActivity.newIntent(requireContext())
                startActivity(intent)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}