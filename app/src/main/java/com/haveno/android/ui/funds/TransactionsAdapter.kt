package com.haveno.android.ui.funds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.haveno.android.R
import com.haveno.android.data.model.Transaction
import com.haveno.android.data.model.TransactionDirection
import com.haveno.android.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * RecyclerView adapter for displaying wallet transactions
 */
class TransactionsAdapter(
    private val onTransactionClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionsAdapter.TransactionViewHolder>(TransactionDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return TransactionViewHolder(binding, onTransactionClick)
    }
    
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class TransactionViewHolder(
        private val binding: ItemTransactionBinding,
        private val onTransactionClick: (Transaction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        private val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        
        fun bind(transaction: Transaction) {
            with(binding) {
                // Transaction type and direction
                textTransactionType.text = transaction.getTypeDisplayName()
                textTransactionType.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        when (transaction.direction) {
                            TransactionDirection.INCOMING -> R.color.status_connected
                            TransactionDirection.OUTGOING -> R.color.status_error
                        }
                    )
                )
                
                // Amount with direction prefix
                textAmount.text = transaction.getDisplayAmount()
                textAmount.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        when (transaction.direction) {
                            TransactionDirection.INCOMING -> R.color.status_connected
                            TransactionDirection.OUTGOING -> R.color.status_error
                        }
                    )
                )
                
                // Fee (if applicable)
                val feeText = transaction.getDisplayFee()
                if (feeText != null) {
                    textFee.text = "Fee: $feeText"
                    textFee.visibility = android.view.View.VISIBLE
                } else {
                    textFee.visibility = android.view.View.GONE
                }
                
                // Confirmation status
                textConfirmationStatus.text = transaction.getConfirmationStatus()
                textConfirmationStatus.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        if (transaction.isConfirmed) R.color.status_connected else R.color.status_connecting
                    )
                )
                
                // Transaction ID (shortened)
                textTransactionId.text = "ID: ${transaction.getShortTxId()}"
                
                // Timestamp
                textTimestamp.text = dateFormat.format(transaction.timestamp)
                
                // Memo (if available)
                if (transaction.memo != null) {
                    textMemo.text = transaction.memo
                    textMemo.visibility = android.view.View.VISIBLE
                } else {
                    textMemo.visibility = android.view.View.GONE
                }
                
                // Trading peer (if applicable)
                if (transaction.tradingPeer != null) {
                    textTradingPeer.text = "Peer: ${transaction.tradingPeer}"
                    textTradingPeer.visibility = android.view.View.VISIBLE
                } else {
                    textTradingPeer.visibility = android.view.View.GONE
                }
                
                // Status indicator
                val statusColor = when {
                    transaction.needsAttention() -> R.color.status_error
                    !transaction.isConfirmed -> R.color.status_connecting
                    else -> R.color.status_connected
                }
                
                indicatorStatus.setColorFilter(
                    ContextCompat.getColor(root.context, statusColor)
                )
                
                // Direction icon
                iconDirection.setImageResource(
                    when (transaction.direction) {
                        TransactionDirection.INCOMING -> R.drawable.ic_receive
                        TransactionDirection.OUTGOING -> R.drawable.ic_send
                    }
                )
                
                iconDirection.setColorFilter(
                    ContextCompat.getColor(
                        root.context,
                        when (transaction.direction) {
                            TransactionDirection.INCOMING -> R.color.status_connected
                            TransactionDirection.OUTGOING -> R.color.status_error
                        }
                    )
                )
                
                // Click listener
                root.setOnClickListener {
                    onTransactionClick(transaction)
                }
                
                // Highlight unconfirmed transactions
                root.alpha = if (transaction.isConfirmed) 1.0f else 0.8f
            }
        }
    }
    
    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.txId == newItem.txId
        }
        
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}