package com.example.drivenext.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivenext.databinding.FragmentHomeBinding
import com.example.drivenext.utils.CarRepository
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var carAdapter: CarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        loadCars()

        binding.btnRetry.setOnClickListener {
            loadCars()
        }
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            cars = emptyList(),
            onBookClick = { car ->
                Toast.makeText(requireContext(), "Переход к оформлению: ${car.model}", Toast.LENGTH_SHORT).show()
            },
            onDetailsClick = { car ->
                Toast.makeText(requireContext(), "Детали: ${car.brand} ${car.model}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvCars.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCars.adapter = carAdapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    val intent = Intent(requireContext(), SearchResultsActivity::class.java).apply {
                        putExtra(SearchResultsActivity.EXTRA_QUERY, query.trim())
                    }
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    private fun loadCars() {
        binding.pbLoading.visibility = View.VISIBLE
        binding.layoutError.visibility = View.GONE
        binding.rvCars.visibility = View.GONE

        lifecycleScope.launch {
            val result = CarRepository.getAvailableCars()
            binding.pbLoading.visibility = View.GONE
            result.onSuccess { list ->
                binding.rvCars.visibility = View.VISIBLE
                carAdapter.updateData(list)
            }.onFailure {
                binding.layoutError.visibility = View.VISIBLE
                binding.tvErrorMessage.text = "Не удалось загрузить данные. Попробуйте снова."
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}