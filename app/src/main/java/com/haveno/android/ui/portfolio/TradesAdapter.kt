package com.haveno.android.ui.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.haveno.android.R
import com.haveno.android.data.model.Trade
import com.haveno.android.data.model.TradeRole
import com.haveno.android.databinding.ItemTradeBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * RecyclerView adapter for displaying active trades
 */
class TradesAdapter(
    private val onTradeClick: (Trade) -> Unit
) : ListAdapter<Trade, TradesAdapter.TradeViewHolder>(TradeDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeViewHolder {
        val binding = ItemTradeBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return TradeViewHolder(binding, onTradeClick)
    }
    
    override fun onBindViewHolder(holder: TradeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class TradeViewHolder(
        private val binding: ItemTradeBinding,
        private val onTradeClick: (Trade) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        private val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        
        fun bind(trade: Trade) {
            with(binding) {
                // Trade ID and role
                textTradeId.text = "Trade #${trade.shortId}"
                textTradeRole.text = trade.role.getDisplayName()
                textTradeRole.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        if (trade.role == TradeRole.BUYER) R.color.status_connected else R.color.status_error
                    )
                )
                
                // Amount and price
                textAmount.text = trade.getDisplayAmount()
                textPrice.text = trade.getDisplayPrice()
                textVolume.text = trade.getDisplayVolume()
                
                // Payment method
                textPaymentMethod.text = trade.paymentMethod
                
                // Status
                textStatus.text = trade.getStatusText()
                
                // Set status color based on trade state
                val statusColor = when {
                    trade.isFinal() -> R.color.status_connected
                    trade.errorMessage != null -> R.color.status_error
                    else -> R.color.status_connecting
                }
                textStatus.setTextColor(ContextCompat.getColor(root.context, statusColor))
                
                // Trade date
                textTradeDate.text = dateFormat.format(trade.tradeDate)
                
                // Trading peer
                textTradingPeer.text = "Peer: ${trade.tradingPeerNodeAddress.take(8)}..."
                
                // Progress indicators
                updateProgressIndicators(trade)
                
                // Click listener
                root.setOnClickListener {
                    onTradeClick(trade)
                }
            }
        }
        
        private fun updateProgressIndicators(trade: Trade) {
            with(binding) {
                // Deposit status
                indicatorDeposit.setColorFilter(
                    ContextCompat.getColor(
                        root.context,
                        if (trade.isDepositConfirmed) R.color.status_connected else R.color.status_connecting
                    )
                )
                
                // Payment status
                indicatorPayment.setColorFilter(
                    ContextCompat.getColor(
                        root.context,
                        when {
                            trade.isPaymentReceived -> R.color.status_connected
                            trade.isPaymentSent -> R.color.status_connecting
                            else -> R.color.status_disconnected
                        }
                    )
                )
                
                // Completion status
                indicatorCompletion.setColorFilter(
                    ContextCompat.getColor(
                        root.context,
                        if (trade.isCompleted) R.color.status_connected else R.color.status_disconnected
                    )
                )
            }
        }
    }
    
    class TradeDiffCallback : DiffUtil.ItemCallback<Trade>() {
        override fun areItemsTheSame(oldItem: Trade, newItem: Trade): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Trade, newItem: Trade): Boolean {
            return oldItem == newItem
        }
    }
}