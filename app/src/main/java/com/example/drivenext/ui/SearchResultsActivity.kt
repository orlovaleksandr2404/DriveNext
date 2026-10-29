package com.example.drivenext.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivenext.databinding.ActivitySearchResultsBinding
import com.example.drivenext.utils.CarRepository
import kotlinx.coroutines.launch

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultsBinding
    private lateinit var carAdapter: CarAdapter
    private var currentQuery: String = ""

    companion object {
        const val EXTRA_QUERY = "extra_query"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentQuery = intent.getStringExtra(EXTRA_QUERY).orEmpty()

        binding.btnBack.setOnClickListener { finish() }
        setupRecyclerView()
        performSearch(currentQuery)

        binding.btnRetry.setOnClickListener {
            performSearch(currentQuery)
        }
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            cars = emptyList(),
            onBookClick = { car ->
                Toast.makeText(this, "Бронирование: ${car.model}", Toast.LENGTH_SHORT).show()
            },
            onDetailsClick = { car ->
                Toast.makeText(this, "Детали: ${car.model}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvResults.layoutManager = LinearLayoutManager(this)
        binding.rvResults.adapter = carAdapter
    }

    private fun performSearch(query: String) {
        binding.pbLoading.visibility = View.VISIBLE
        binding.layoutError.visibility = View.GONE
        binding.rvResults.visibility = View.GONE

        lifecycleScope.launch {
            val result = CarRepository.searchCars(query)
            binding.pbLoading.visibility = View.GONE
            result.onSuccess { list ->
                if (list.isEmpty()) {
                    binding.layoutError.visibility = View.VISIBLE
                    binding.tvErrorMessage.text = "Ничего не найдено по запросу \"$query\""
                    binding.btnRetry.visibility = View.GONE
                } else {
                    binding.rvResults.visibility = View.VISIBLE
                    carAdapter.updateData(list)
                }
            }.onFailure {
                binding.layoutError.visibility = View.VISIBLE
                binding.btnRetry.visibility = View.VISIBLE
                binding.tvErrorMessage.text = "Не удалось выполнить поиск. Попробуйте снова."
            }
        }
    }
}