package com.nile.fortu.game

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hbb20.CountryCodePicker.PhoneNumberValidityChangeListener
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

        binding.ccp.registerCarrierNumberEditText(binding.etPhoneNumber)

        binding.flContinue.setOnClickListener {
            val intent = MenuActivity.newIntent(requireContext())
            if (binding.etPhoneNumber.text?.isNotBlank() == true && binding.ccp.isValidFullNumber) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT).show()
            }
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