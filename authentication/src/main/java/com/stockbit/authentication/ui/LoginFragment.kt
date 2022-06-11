package com.stockbit.authentication.ui

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.stockbit.authentication.databinding.FragmentLoginBinding
import com.stockbit.authentication.viewmodel.AuthenticationViewModel
import com.stockbit.common.R
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.common.extension.showSnackbar

class LoginFragment : BaseFragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var cancellationSignal: CancellationSignal? = null

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
        binding.btnLoginFingerprint.setOnClickListener {
            if (checkBiometricSupport()) loginFingerprint()
        }
    }

    private fun goToWatchlist() {
        val request = NavDeepLinkRequest.Builder
            .fromUri("stockbit://watchlist".toUri())
            .build()

        findNavController().navigate(request)
    }

    private fun loginFingerprint() {
        val biometricPrompt = BiometricPrompt.Builder(requireContext())
            .setTitle(requireContext().resources.getString(R.string.authentication))
            .setSubtitle(requireContext().resources.getString(R.string.touch_fingerprint))
            .setDescription(requireContext().resources.getString(R.string.fingerprint))
            .setNegativeButton(requireContext().resources.getString(R.string.cancel), requireContext().mainExecutor, DialogInterface.OnClickListener { dialogInterface, i ->
            }).build()
        biometricPrompt.authenticate(getCancellationSignal(), requireContext().mainExecutor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                showSnackbar("Authentication error : $errString", Snackbar.LENGTH_SHORT)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                goToWatchlist()
            }
        })
    }

    private fun getCancellationSignal() : CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            showSnackbar("Authentication was cancelled by user", Snackbar.LENGTH_SHORT)
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport() : Boolean {
        val keyguardManager = requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure){
            showSnackbar("Finerprint has not been enable in settings.", Snackbar.LENGTH_SHORT)
            return false
        }
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            showSnackbar("Finerprint has not been enable in settings.", Snackbar.LENGTH_SHORT)
            return false
        }
        return if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        } else false
    }

}