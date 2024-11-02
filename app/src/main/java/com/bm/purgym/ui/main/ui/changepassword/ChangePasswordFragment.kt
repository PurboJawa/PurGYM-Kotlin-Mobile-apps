package com.bm.purgym.ui.main.ui.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bm.purgym.data.models.AdminModel
import com.bm.purgym.databinding.FragmentChangePasswordBinding
import com.bm.purgym.ui.auth.register.RegisterViewModel
import com.bm.purgym.ui.main.MainActivity
import com.bm.purgym.utils.Constants.alertDialogMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ChangePasswordViewModel>()
    private var adminId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root
        adminId = (requireActivity() as MainActivity).adminId

        setListeners()

        return root
    }

    private fun setListeners() {
        binding.apply {
            btnSave.setOnClickListener {
                if (isValid()) {
                    viewModel.changeAdminPassword(adminId, binding.edPassword.text.toString())
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setCancelable(false)

                    with(builder)
                    {
                        setTitle("Sukses Ganti Password Admin!")
                        setMessage("Klik OK untuk melanjutkan.")
                        setPositiveButton("OK") { dialog, _ ->
                            edPassword.setText("")
                            dialog.dismiss()
                        }
                        show()
                    }
                }
            }
        }
    }

    private fun isValid() = if (binding.edPassword.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan password dengan benar!")
        false
    } else if (binding.edPassword.text.toString().length < 8) {
        alertDialogMessage(requireContext(), "Password harus memiliki minimal 8 huruf!")
        false
    } else {
        true
    }
}