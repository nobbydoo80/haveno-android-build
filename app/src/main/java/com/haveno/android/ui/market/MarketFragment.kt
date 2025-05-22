package com.haveno.android.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.AdapterView
import com.haveno.android.databinding.FragmentMarketBinding
import timber.log.Timber

class MarketFragment : Fragment() {
    
    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MarketViewModel by viewModels()
    private lateinit var orderBookAdapter: OrderBookAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Timber.d("=== MARKET FRAGMENT LOADED ===")
        
        // Show debug message in error text view
        binding.textError.apply {
            text = "📈 MARKET FRAGMENT LOADED!\n\n" +
                   "✅ Navigation working\n" +
                   "✅ Fragment inflated\n" +
                   "✅ ViewBinding working\n\n" +
                   "This would show:\n" +
                   "• Order book\n" +
                   "• Market prices\n" +
                   "• Trading pairs\n\n" +
                   "Mock data ready!"
            visibility = View.VISIBLE
        }
        
        setupOrderBook()
        setupObservers()
        setupClickListeners()
        
        // Load initial data
        viewModel.loadOrderBook("USD") // Default to USD
    }
    
    private fun setupOrderBook() {
        orderBookAdapter = OrderBookAdapter { offer ->
            // Handle offer click - navigate to trade creation
            viewModel.selectOffer(offer)
        }
        
        binding.recyclerViewOrderBook.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = orderBookAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.orderBook.observe(viewLifecycleOwner) { orders ->
            orderBookAdapter.submitList(orders)
            updateEmptyState(orders.isEmpty())
        }
        
        viewModel.selectedCurrency.observe(viewLifecycleOwner) { currency ->
            binding.textSelectedCurrency.text = currency
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                // Show error message
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = it
            } ?: run {
                binding.textError.visibility = View.GONE
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.buttonRefresh.setOnClickListener {
            viewModel.refreshOrderBook()
        }
        
        binding.spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val currencies = listOf("USD", "EUR", "GBP", "JPY", "CAD")
                viewModel.loadOrderBook(currencies[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        
        binding.fabCreateOffer.setOnClickListener {
            // Navigate to create offer screen
            // findNavController().navigate(R.id.action_market_to_create_offer)
        }
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        binding.layoutEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewOrderBook.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}