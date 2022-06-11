package com.watchlist.watchlist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
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

    private var currentPage = 0
    private var totalAvailablePages = 5
    private lateinit var arrWatchlist: ArrayList<WatchlistModel>

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
        arrWatchlist = ArrayList()
        initActions()
        observer()
    }

    private fun initActions() {
        binding.swipeContainer.setOnRefreshListener {
            getWatclistData()
        }
        binding.rvWatchlist.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.rvWatchlist.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1
                        getWatclistData()
                    }
                }
            }
        })
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

    private fun loadMoreLoading() {
        if (currentPage == 1) {
            if (binding.progressLoadMore.isShown) {
                binding.progressLoadMore.visibility = View.GONE
            } else {
                binding.progressLoadMore.visibility = View.VISIBLE
            }
        } else {
            if (binding.progressLoadMore.isShown) {
                binding.progressLoadMore.visibility = View.GONE
            } else {
                binding.progressLoadMore.visibility = View.VISIBLE
            }
        }
    }

    fun setWatchlistRecyclerView(watchlist : List<WatchlistModel>) {
        val oldCount = arrWatchlist.size
        arrWatchlist.addAll(watchlist)

        val watchlisAdapter = WatchlistAdapter() {
            if (it != null) {
                Toast.makeText(requireContext(), it.fullname, Toast.LENGTH_SHORT).show()
            }
        }

        watchlisAdapter.setData(arrWatchlist, oldCount, arrWatchlist.size)
        with(binding.rvWatchlist) {
            setHasFixedSize(true)
            adapter = watchlisAdapter
        }
    }

    fun getWatclistData() {
        viewModel.getWatchlist(currentPage)
    }
}