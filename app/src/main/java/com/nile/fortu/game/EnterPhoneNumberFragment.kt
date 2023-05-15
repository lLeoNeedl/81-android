package com.nile.fortu.game

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import com.nile.fortu.game.databinding.FragmentAuthorizationBinding
import com.nile.fortu.game.databinding.FragmentEnterPhoneNumberBinding

class EnterPhoneNumberFragment : Fragment() {

    private var _binding: FragmentEnterPhoneNumberBinding? = null
    private val binding: FragmentEnterPhoneNumberBinding
        get() = _binding ?: throw RuntimeException("EnterPhoneNumberFragment == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.flContinue.setOnClickListener {
            val intent = MenuActivity.newIntent(requireContext())
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = EnterPhoneNumberFragment()
    }
}