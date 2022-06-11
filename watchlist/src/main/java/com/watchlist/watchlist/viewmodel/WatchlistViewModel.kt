package com.watchlist.watchlist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.stockbit.common.base.BaseViewModel
import com.stockbit.model.WatchlistModel
import com.stockbit.repository.WatchlistRepository
import com.stockbit.repository.utils.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WatchlistViewModel(private val watchlistRepository: WatchlistRepository) : BaseViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _watchlistData = MutableLiveData<List<WatchlistModel>>()
    val watchlistData: LiveData<List<WatchlistModel>>
        get() = _watchlistData

    init {
        getWatchlist(0)
    }

    fun getWatchlist(page : Int) = viewModelScope.launch {
        watchlistRepository.getWatchlist(page).collect { res ->
            when(res.status) {
                Resource.Status.SUCCESS -> {
                    _watchlistData.postValue(res.data)
                    _loading.postValue(false)
                }
                Resource.Status.LOADING -> {
                    _loading.postValue(true)
                }
                Resource.Status.ERROR -> {
                    _watchlistData.postValue(res.data)
                    _errorMessage.postValue(res.error?.message.toString())
                    _loading.postValue(false)
                }
            }
        }
    }
}