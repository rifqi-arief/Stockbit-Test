package com.stockbit.portofolio.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.portofolio.databinding.FragmentPortofolioBinding
import com.stockbit.portofolio.viewmodel.PortofolioViewModel

class PortofolioFragment : BaseFragment() {

    private var _binding : FragmentPortofolioBinding? = null
    private val binding get() = _binding!!

    override fun getViewModel(): BaseViewModel = PortofolioViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPortofolioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActions()
    }

    private fun initActions(){

    }
}