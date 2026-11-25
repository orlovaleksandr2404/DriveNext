package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivenext.database.AppDatabase
import com.example.drivenext.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var carAdapter: CarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carAdapter = CarAdapter(
            cars = emptyList(),
            onBookClick = { car ->
                val intent = Intent(requireContext(), BookingActivity::class.java).apply {
                    putExtra(BookingActivity.EXTRA_CAR, car)
                }
                startActivity(intent)
            },
            onDetailsClick = { car ->
                val intent = Intent(requireContext(), CarDetailsActivity::class.java).apply {
                    putExtra(CarDetailsActivity.EXTRA_CAR, car)
                }
                startActivity(intent)
            }
        )

        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = carAdapter
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            val favoriteEntities = AppDatabase.getInstance(requireContext())
                .favoriteDao()
                .getAllFavorites()
            val cars = favoriteEntities.map { it.toCarItem() }

            if (cars.isEmpty()) {
                binding.tvEmptyFavorites.visibility = View.VISIBLE
                binding.rvFavorites.visibility = View.GONE
            } else {
                binding.tvEmptyFavorites.visibility = View.GONE
                binding.rvFavorites.visibility = View.VISIBLE
                carAdapter.updateData(cars)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}