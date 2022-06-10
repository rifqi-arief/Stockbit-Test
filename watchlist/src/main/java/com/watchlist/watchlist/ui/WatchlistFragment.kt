package com.watchlist.watchlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.watchlist.watchlist.databinding.FragmentWatchlistBinding
import com.watchlist.watchlist.viewmodel.WatchlistViewModel

class WatchlistFragment : BaseFragment() {
    private var _binding : FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    override fun getViewModel(): BaseViewModel = WatchlistViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActions()
    }

    private fun initActions(){

    }
}