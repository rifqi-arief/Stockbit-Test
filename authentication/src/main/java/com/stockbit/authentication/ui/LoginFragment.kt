package com.stockbit.authentication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.stockbit.authentication.databinding.FragmentLoginBinding
import com.stockbit.authentication.viewmodel.AuthenticationViewModel
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel

class LoginFragment : BaseFragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun getViewModel(): BaseViewModel = AuthenticationViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActions()
    }

    private fun initActions(){
        binding.btnLogin.setOnClickListener{
            goToWatchlist()
        }
    }

    private fun goToWatchlist() {
        val request = NavDeepLinkRequest.Builder
            .fromUri("stockbit://watchlist".toUri())
            .build()


        findNavController().navigate(request)
    }
}