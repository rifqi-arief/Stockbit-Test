package com.watchlist.watchlist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.common.extension.showSnackbar
import com.stockbit.common.utils.Dialog
import com.stockbit.model.WatchlistModel
import com.watchlist.watchlist.adapter.WatchlistAdapter
import com.watchlist.watchlist.databinding.FragmentWatchlistBinding
import com.watchlist.watchlist.viewmodel.WatchlistViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class WatchlistFragment : BaseFragment() {
    private var _binding : FragmentWatchlistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<WatchlistViewModel>()
    override fun getViewModel(): BaseViewModel = viewModel
    lateinit var loading : android.app.Dialog

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
        loading = Dialog.loading(requireContext())
        initActions()
        observer()
    }

    private fun initActions() {
        binding.swipeContainer.setOnRefreshListener {
            viewModel.getWatchlist()
        }
    }

    private fun observer() {
        viewModel.loading.observe(viewLifecycleOwner, {
            if(it){
                showLoading()
            }else{
                dismissLoading()
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            dismissLoading()
            Log.e("StockbitLogging", it)
            showSnackbar(it, Snackbar.LENGTH_SHORT)
        })
        viewModel.watchlistData.observe(viewLifecycleOwner, {
            if (it.size > 0) {
                setWatchlistRecyclerView(it)
            }
            binding.swipeContainer.isRefreshing = false
        })
    }

    fun showLoading(){
        if (!loading.isShowing){
            loading.show()
        }
    }

    fun dismissLoading(){
        if(loading.isShowing){
            loading.dismiss()
        }
    }

    fun setWatchlistRecyclerView(watchlist : List<WatchlistModel>){
        val watchlisAdapter = WatchlistAdapter(watchlist){
            if (it != null) {
            }
        }

        with(binding.rvWatchlist){
            setHasFixedSize(true)
            adapter = watchlisAdapter
        }
    }

}