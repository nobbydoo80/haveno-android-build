package com.haveno.android.ui.funds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.haveno.android.databinding.FragmentFundsBinding

class FundsFragment : Fragment() {
    
    private var _binding: FragmentFundsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FundsViewModel by viewModels()
    private lateinit var transactionsAdapter: TransactionsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFundsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupTransactions()
        setupObservers()
        setupClickListeners()
        setupTabs()
        
        // Load initial data
        viewModel.loadWalletData()
    }
    
    private fun setupTransactions() {
        transactionsAdapter = TransactionsAdapter { transaction ->
            // Handle transaction click - show details
            viewModel.selectTransaction(transaction)
        }
        
        binding.recyclerViewTransactions.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionsAdapter
        }
    }
    
    private fun setupObservers() {
        // Wallet balance
        viewModel.walletBalance.observe(viewLifecycleOwner) { balance ->
            balance?.let { updateBalanceDisplay(it) }
        }
        
        // Transactions
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            transactionsAdapter.submitList(transactions)
            updateTransactionsEmptyState(transactions.isEmpty())
        }
        
        // Loading states
        viewModel.isLoadingBalance.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarBalance.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.isLoadingTransactions.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarTransactions.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        // Error handling
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = it
            } ?: run {
                binding.textError.visibility = View.GONE
            }
        }
        
        // Receive address
        viewModel.receiveAddress.observe(viewLifecycleOwner) { address ->
            address?.let {
                binding.textReceiveAddress.text = formatAddress(it)
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.buttonRefresh.setOnClickListener {
            viewModel.refreshWalletData()
        }
        
        binding.buttonReceive.setOnClickListener {
            viewModel.generateNewReceiveAddress()
            // TODO: Show QR code dialog
        }
        
        binding.buttonSend.setOnClickListener {
            // TODO: Navigate to send transaction screen
        }
        
        binding.cardAvailableBalance.setOnClickListener {
            // Show balance details
            viewModel.showBalanceDetails()
        }
        
        binding.textReceiveAddress.setOnClickListener {
            // Copy address to clipboard
            viewModel.copyAddressToClipboard()
        }
    }
    
    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.filterTransactions("ALL")
                    1 -> viewModel.filterTransactions("INCOMING")
                    2 -> viewModel.filterTransactions("OUTGOING")
                    3 -> viewModel.filterTransactions("PENDING")
                }
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun updateBalanceDisplay(balance: com.haveno.android.data.model.WalletBalance) {
        with(binding) {
            textAvailableBalance.text = balance.getDisplayAvailableBalance()
            textTotalBalance.text = balance.getDisplayTotalBalance()
            textPendingBalance.text = balance.getDisplayPendingBalance()
            textReservedBalance.text = balance.getDisplayReservedBalance()
            
            // Show/hide pending and reserved sections based on amounts
            layoutPendingBalance.visibility = 
                if (balance.hasPendingTransactions()) View.VISIBLE else View.GONE
            layoutReservedBalance.visibility = 
                if (balance.hasReservedFunds()) View.VISIBLE else View.GONE
        }
    }
    
    private fun updateTransactionsEmptyState(isEmpty: Boolean) {
        binding.layoutEmptyTransactions.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewTransactions.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    private fun formatAddress(address: String): String {
        return if (address.length > 20) {
            "${address.substring(0, 10)}...${address.substring(address.length - 10)}"
        } else {
            address
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}