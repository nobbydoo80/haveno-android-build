package com.haveno.android.ui.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.haveno.android.R
import com.haveno.android.data.model.Offer
import com.haveno.android.data.model.OfferDirection
import com.haveno.android.databinding.ItemOrderBookBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * RecyclerView adapter for displaying the order book (list of offers)
 */
class OrderBookAdapter(
    private val onOfferClick: (Offer) -> Unit
) : ListAdapter<Offer, OrderBookAdapter.OrderBookViewHolder>(OfferDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderBookViewHolder {
        val binding = ItemOrderBookBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return OrderBookViewHolder(binding, onOfferClick)
    }
    
    override fun onBindViewHolder(holder: OrderBookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class OrderBookViewHolder(
        private val binding: ItemOrderBookBinding,
        private val onOfferClick: (Offer) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        private val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        
        fun bind(offer: Offer) {
            with(binding) {
                // Set offer direction and color
                textDirection.text = offer.direction.getDisplayName()
                textDirection.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        if (offer.direction == OfferDirection.BUY) R.color.status_connected else R.color.status_error
                    )
                )
                
                // Set price
                textPrice.text = offer.getDisplayPrice()
                
                // Set amount range
                textAmount.text = offer.getDisplayRange()
                
                // Set volume
                val volume = "${offer.getVolume().toPlainString()} ${offer.currencyCode}"
                textVolume.text = volume
                
                // Set payment method
                textPaymentMethod.text = offer.paymentMethod
                
                // Set creation date
                textDate.text = dateFormat.format(offer.creationDate)
                
                // Highlight own offers
                root.setBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        if (offer.isMyOffer) R.color.haveno_primary_light else android.R.color.transparent
                    )
                )
                
                // Set click listener
                root.setOnClickListener {
                    onOfferClick(offer)
                }
                
                // Add visual indicator for own offer
                if (offer.isMyOffer) {
                    textDirection.text = "${offer.direction.getDisplayName()} (Mine)"
                }
            }
        }
    }
    
    class OfferDiffCallback : DiffUtil.ItemCallback<Offer>() {
        override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem == newItem
        }
    }
}