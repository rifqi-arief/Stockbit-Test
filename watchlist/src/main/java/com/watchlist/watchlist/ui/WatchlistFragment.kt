package com.watchlist.watchlist.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.stockbit.common.R
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
    private var doubleBackToExitPressedOnce = false

    private var currentPage = 0
    private var totalAvailablePages = 5
    private lateinit var arrWatchlist: ArrayList<WatchlistModel>
    private lateinit var recyclerViewState : Parcelable

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
        val backCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {
                        activity?.finishAffinity()
                        return
                    }
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(requireContext(), requireActivity().getString(R.string.exit_button), Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
                }
            }

        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), backCallback);

        binding.swipeContainer.setOnRefreshListener {
            currentPage = 0
            arrWatchlist.clear()
            getWatclistData()
        }

        binding.rvWatchlist.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.rvWatchlist.canScrollVertically(1)) {
                        if (currentPage < totalAvailablePages) {
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

    @SuppressLint("NotifyDataSetChanged")
    fun setWatchlistRecyclerView(watchlist : List<WatchlistModel>) {
        recyclerViewState = binding.rvWatchlist.layoutManager?.onSaveInstanceState()!!
        arrWatchlist.addAll(watchlist)

        val watchlisAdapter = WatchlistAdapter() {
            if (it != null) {
                Toast.makeText(requireContext(), it.fullname, Toast.LENGTH_SHORT).show()
            }
        }

        watchlisAdapter.notifyDataSetChanged()
        watchlisAdapter.setData(arrWatchlist)
        with(binding.rvWatchlist) {
            adapter = watchlisAdapter
            layoutManager?.onRestoreInstanceState(recyclerViewState)
        }
    }

    fun getWatclistData() {
        viewModel.getWatchlist(currentPage)
    }
}