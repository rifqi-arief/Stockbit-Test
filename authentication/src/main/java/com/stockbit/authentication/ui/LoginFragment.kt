package com.stockbit.authentication.ui

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import com.stockbit.authentication.databinding.FragmentLoginBinding
import com.stockbit.authentication.viewmodel.AuthenticationViewModel
import com.stockbit.common.R
import com.stockbit.common.base.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.extension.showSnackbar
import java.util.*


class LoginFragment : BaseFragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var cancellationSignal: CancellationSignal? = null
    lateinit var callbackManager : CallbackManager

    override fun getViewModel(): BaseViewModel = AuthenticationViewModel()

    lateinit var gso : GoogleSignInOptions
    lateinit var gsc : GoogleSignInClient

    val GOOGLE_REQUEST_CODE = 100

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
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)
        initLoginFacebook()
    }

    private fun initActions(){
        binding.btnLogin.setOnClickListener{
            goToWatchlist()
        }
        binding.btnLoginFingerprint.setOnClickListener {
            if (checkBiometricSupport()) loginFingerprint()
        }
        binding.btnLoginFacebook.setOnClickListener{
            loginFacebook()
        }
        binding.btnLoginGoogle.setOnClickListener {
            loginGoogle()
        }
    }

    private fun goToWatchlist() {
        if (!checkLogin()){
            return
        }
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
            showSnackbar(requireContext().getString(R.string.fingerprint_not_enable), Snackbar.LENGTH_SHORT)
            return false
        }
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            showSnackbar(requireContext().getString(R.string.fingerprint_not_enable), Snackbar.LENGTH_SHORT)
            return false
        }
        return if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        } else false
    }

    fun loginGoogle() {
        val loginGoogleIntent = gsc.signInIntent
        startActivityForResult(loginGoogleIntent, GOOGLE_REQUEST_CODE)
    }

    private fun initLoginFacebook() {
        FacebookSdk.sdkInitialize(requireActivity())
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object :FacebookCallback<LoginResult> {
                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                    showSnackbar("Error ${error.message}", Snackbar.LENGTH_SHORT)
                }

                override fun onSuccess(result: LoginResult) {
                    goToWatchlist()
                }
            })
    }

    private fun loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_REQUEST_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                goToWatchlist()
            } catch (e : ApiException) {
                showSnackbar(requireContext().getString(R.string.something_wrong), Snackbar.LENGTH_SHORT)
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    fun checkLogin() : Boolean{
        var result = true
        if (binding.etLoginUsername.text.isNullOrEmpty()){
            binding.layoutLoginUsername.isErrorEnabled = true
            binding.layoutLoginUsername.error = requireContext().getString(R.string.required)
            result = false
        }else{
            binding.layoutLoginUsername.isErrorEnabled = false
        }
        if (binding.etLoginPassword.text.isNullOrEmpty()){
            binding.layoutLoginPassword.isErrorEnabled = true
            binding.layoutLoginPassword.error = requireContext().getString(R.string.required)
            result = false
        }else{
            binding.layoutLoginPassword.isErrorEnabled = false
        }

        return result
    }
}