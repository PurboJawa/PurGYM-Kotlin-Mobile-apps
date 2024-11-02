package com.bm.purgym.ui.auth.register

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bm.purgym.R
import com.bm.purgym.data.models.AdminModel
import com.bm.purgym.databinding.FragmentRegisterBinding
import com.bm.purgym.ui.auth.login.LoginFragment
import com.bm.purgym.utils.Constants.alertDialogMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    private val viewModel by viewModel<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        setupPlayAnimation()
        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                if (isValid()) {
                    val adminModel = AdminModel(
                        username = binding.edUsername.text.toString(),
                        password = binding.edPassword.text.toString()
                    )
                    viewModel.register(adminModel)
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setCancelable(false)

                    with(builder)
                    {
                        setTitle("Sukses Register")
                        setMessage("Klik OK untuk melanjutkan.")
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            changeToLogin()
                        }
                        show()
                    }
                }
            }

            btnLogin.setOnClickListener { changeToLogin() }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                changeToLogin()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun changeToLogin() {
        parentFragmentManager.beginTransaction().apply {
            replace(
                R.id.auth_container,
                LoginFragment(),
                LoginFragment::class.java.simpleName
            )
            commit()
        }
    }

    private fun isValid() = if (binding.edUsername.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan Username dengan benar!")
        false
    } else if (binding.edPassword.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan password dengan benar!")
        false
    } else if (binding.edPassword.text.toString().length < 8) {
        alertDialogMessage(requireContext(), "Password harus memiliki minimal 8 huruf!")
        false
    } else {
        true
    }

    @SuppressLint("Recycle")
    private fun setupPlayAnimation() {
        val email: Animator =
            ObjectAnimator.ofFloat(binding.edEmailLayout, View.ALPHA, 1f).setDuration(150)
        val password: Animator =
            ObjectAnimator.ofFloat(binding.edPasswordLayout, View.ALPHA, 1f).setDuration(150)
        val button: Animator =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(150)
        val layoutText: Animator =
            ObjectAnimator.ofFloat(binding.layoutText, View.ALPHA, 1f).setDuration(150)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(email, password, button, layoutText)
        animatorSet.startDelay = 150
        animatorSet.start()
    }
}