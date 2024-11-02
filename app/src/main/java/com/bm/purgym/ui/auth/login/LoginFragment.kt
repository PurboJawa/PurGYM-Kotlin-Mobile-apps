package com.bm.purgym.ui.auth.login

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bm.purgym.R
import com.bm.purgym.data.models.AdminModel
import com.bm.purgym.databinding.FragmentLoginBinding
import com.bm.purgym.ui.auth.register.RegisterFragment
import com.bm.purgym.ui.main.MainActivity
import com.bm.purgym.utils.Constants.alertDialogMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        setupPlayAnimation()
        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (isValid()) {
                    val adminModel = AdminModel(
                        username = binding.edUsername.text.toString(),
                        password = binding.edPassword.text.toString()
                    )
                    val isLogin = viewModel.login(adminModel)
                    if (isLogin) {
                        val iMain = Intent(requireActivity(), MainActivity::class.java)
                        iMain.putExtra(MainActivity.EXTRA_ADMIN_ID, adminModel.adminId)
                        requireActivity().finishAffinity()
                        startActivity(iMain)
                    } else {
                        alertDialogMessage(requireContext(), "Akun tidak ter-Autentikasi!")
                    }
                }
            }

            btnRegister.setOnClickListener { changeToRegister() }
        }
    }

    private fun isValid() = if (binding.edUsername.text.isNullOrEmpty()
    ) {
        alertDialogMessage(requireContext(), "Masukkan username dengan benar!")
        false
    } else if (binding.edPassword.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan password dengan benar!")
        false
    } else {
        true
    }


    private fun changeToRegister() {
        parentFragmentManager.beginTransaction().apply {
            replace(
                R.id.auth_container,
                RegisterFragment(),
                RegisterFragment::class.java.simpleName
            )
            commit()
        }
    }

    @SuppressLint("Recycle")
    private fun setupPlayAnimation() {
        val email: Animator =
            ObjectAnimator.ofFloat(binding.edEmailLayout, View.ALPHA, 1f).setDuration(150)
        val password: Animator =
            ObjectAnimator.ofFloat(binding.edPasswordLayout, View.ALPHA, 1f).setDuration(150)
        val button: Animator =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(150)
        val layoutText: Animator =
            ObjectAnimator.ofFloat(binding.layoutText, View.ALPHA, 1f).setDuration(150)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(email, password, button, layoutText)
        animatorSet.startDelay = 150
        animatorSet.start()
    }
}