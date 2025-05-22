package com.haveno.android.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.haveno.android.databinding.FragmentPortfolioBinding
import timber.log.Timber

class PortfolioFragment : Fragment() {
    
    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: PortfolioViewModel by viewModels()
    private lateinit var tradesAdapter: TradesAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortfolioBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Timber.d("=== PORTFOLIO FRAGMENT LOADED ===")
        
        // Show debug message in error text view
        binding.textError.apply {
            text = "💼 PORTFOLIO FRAGMENT LOADED!\n\n" +
                   "✅ Navigation working\n" +
                   "✅ Fragment inflated\n" +
                   "✅ ViewBinding working\n\n" +
                   "This would show:\n" +
                   "• Active trades\n" +
                   "• Trade history\n" +
                   "• Open orders\n\n" +
                   "Mock data ready!"
            visibility = View.VISIBLE
        }
        
        setupTrades()
        setupObservers()
        setupClickListeners()
        
        // Load initial data
        viewModel.loadTrades()
    }
    
    private fun setupTrades() {
        tradesAdapter = TradesAdapter { trade ->
            // Handle trade click - navigate to trade detail
            viewModel.selectTrade(trade)
        }
        
        binding.recyclerViewTrades.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tradesAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.trades.observe(viewLifecycleOwner) { trades ->
            tradesAdapter.submitList(trades)
            updateEmptyState(trades.isEmpty())
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = it
            } ?: run {
                binding.textError.visibility = View.GONE
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.buttonRefresh.setOnClickListener {
            viewModel.refreshTrades()
        }
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        binding.layoutEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewTrades.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}