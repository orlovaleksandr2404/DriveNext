package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.drivenext.databinding.FragmentSettingsBinding
import com.example.drivenext.utils.SessionManager

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedEmail = SessionManager.getUserEmail(requireContext())
        if (!savedEmail.isNullOrBlank()) {
            binding.tvUserEmail.text = savedEmail
        }

        binding.cardUserProfile.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }

        binding.btnMyBookings.setOnClickListener {
            Toast.makeText(requireContext(), "Мои бронирования", Toast.LENGTH_SHORT).show()
        }
        binding.btnConnectCar.setOnClickListener {
            Toast.makeText(requireContext(), "Экран добавления автомобиля", Toast.LENGTH_SHORT).show()
        }
        binding.btnTheme.setOnClickListener {
            Toast.makeText(requireContext(), "Смена темы оформления", Toast.LENGTH_SHORT).show()
        }
        binding.btnNotifications.setOnClickListener {
            Toast.makeText(requireContext(), "Настройки уведомлений", Toast.LENGTH_SHORT).show()
        }
        binding.btnHelp.setOnClickListener {
            Toast.makeText(requireContext(), "Раздел помощи", Toast.LENGTH_SHORT).show()
        }
        binding.btnInviteFriend.setOnClickListener {
            Toast.makeText(requireContext(), "Пригласить друга", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}