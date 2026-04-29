package com.phonespamkiller.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.phonespamkiller.data.BlockedCall
import com.phonespamkiller.databinding.ItemBlockedCallBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BlockedCallsAdapter :
    ListAdapter<BlockedCall, BlockedCallsAdapter.ViewHolder>(DiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy  HH:mm:ss", Locale.getDefault())

    inner class ViewHolder(private val binding: ItemBlockedCallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BlockedCall) {
            binding.tvPhoneNumber.text = item.phoneNumber
            binding.tvTimestamp.text = dateFormat.format(Date(item.timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBlockedCallBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<BlockedCall>() {
        override fun areItemsTheSame(old: BlockedCall, new: BlockedCall) = old.id == new.id
        override fun areContentsTheSame(old: BlockedCall, new: BlockedCall) = old == new
    }
}
