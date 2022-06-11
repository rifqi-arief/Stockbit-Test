package com.watchlist.watchlist.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stockbit.model.WatchlistModel
import com.watchlist.watchlist.databinding.ItemWatchlistBinding


class WatchlistAdapter(private val arrWatchlist: List<WatchlistModel>, private val onSelect: (WatchlistModel?) -> Unit) : RecyclerView.Adapter<WatchlistAdapter.ViewHolder>()  {

    class ViewHolder(private val binding : ItemWatchlistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(watchlist : WatchlistModel, onSelect: (WatchlistModel?) -> Unit){
            with(binding){
                tvWatchlistName.text = watchlist.name
                tvWatchlistFullname.text = watchlist.fullname
                tvWatchlistPrice.text = watchlist.price
                tvWatchlistChange.text = "${watchlist.changeHour} (${watchlist.changePtcHour})"
                if (!watchlist.changePtcHour.isNullOrEmpty()) {
                    if (watchlist.changePtcHour?.first()!!.uppercase().equals("-".uppercase())) {
                        tvWatchlistChange.setTextColor(Color.RED)
                    } else {
                        tvWatchlistChange.setTextColor(Color.GREEN)
                    }
                }
                root.setOnClickListener {
                    onSelect(watchlist)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemsBinding = ItemWatchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchlistAdapter.ViewHolder(itemsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val watchlist = arrWatchlist[position]
        holder.bind(watchlist, onSelect)
    }

    override fun getItemCount(): Int = arrWatchlist.size

    override fun getItemViewType(position: Int): Int = position


}