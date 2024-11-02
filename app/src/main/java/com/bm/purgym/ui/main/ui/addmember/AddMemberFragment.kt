package com.bm.purgym.ui.main.ui.addmember

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bm.purgym.R
import com.bm.purgym.data.models.MemberModel
import com.bm.purgym.databinding.FragmentAddMemberBinding
import com.bm.purgym.utils.Constants
import com.bm.purgym.utils.Constants.alertDialogMessage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar


class AddMemberFragment : Fragment() {

    private var _binding: FragmentAddMemberBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<AddMemberViewModel>()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                requireContext().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                viewModel.uriImage.postValue(uri.toString())
                binding.ivProfile.setImageURI(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setListeners()

        return root
    }

    private fun setListeners() {
        binding.apply {
            ivProfile.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

            edRegistration.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(), { view, year, monthOfYear, dayOfMonth ->
                        val c1 = Calendar.getInstance()
                        c1.set(year, monthOfYear, dayOfMonth);
                        val millis = c1.timeInMillis
                        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                        val strDate = simpleDateFormat.format(c1.time)
                        viewModel.registrationDate.postValue(millis)
                        edRegistration.setText(strDate)
                    }, year, month, day
                )
                datePickerDialog.show()
            }

            edExpration.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(), { view, year, monthOfYear, dayOfMonth ->
                        val c1 = Calendar.getInstance()
                        c1.set(year, monthOfYear, dayOfMonth);
                        val millis = c1.timeInMillis
                        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                        val strDate = simpleDateFormat.format(c1.time)
                        viewModel.expirationDate.postValue(millis)
                        edExpration.setText(strDate)
                    }, year, month, day
                )
                datePickerDialog.show()
            }

            btnSave.setOnClickListener {
                if (isValid()) {
                    val checkedGender = rgGender.checkedRadioButtonId

                    val memberModel = MemberModel()
                    memberModel.profilePic = viewModel.uriImage.value ?: ""
                    memberModel.name = edNama.text.toString()
                    memberModel.gender = if (checkedGender == R.id.rb_male) {
                        Constants.Gender.Male.name
                    } else {
                        Constants.Gender.Female.name
                    }
                    memberModel.age = edUmur.text.toString()
                    memberModel.height = edHeight.text.toString()
                    memberModel.weight = edWeight.text.toString()
                    memberModel.telp = edTelp.text.toString()
                    memberModel.address = edAddress.text.toString()
                    memberModel.registrationDate = viewModel.registrationDate.value ?: 0L
                    memberModel.expirationDate = viewModel.expirationDate.value ?: 0L

                    viewModel.addMember(memberModel)

                    clearAll()
                    alertDialogMessage(requireContext(), "Member berhasil ditambahkan!")
                }
            }
        }
    }

    private fun isValid() = if (viewModel.uriImage.value.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan Foto Profil dengan benar!")
        false
    } else if (binding.edNama.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan Nama dengan benar!")
        false
    } else if (binding.rgGender.checkedRadioButtonId == -1) {
        alertDialogMessage(requireContext(), "Pilih Gender dengan benar!")
        false
    } else if (binding.edUmur.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan Umur dengan benar!")
        false
    } else if (binding.edHeight.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan Tinggi Badan dengan benar!")
        false
    } else if (binding.edWeight.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan Berat Badan dengan benar!")
        false
    } else if (binding.edTelp.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan No. Telp dengan benar!")
        false
    } else if (binding.edAddress.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan Alamat dengan benar!")
        false
    } else if (viewModel.registrationDate.value == 0L || viewModel.registrationDate.value == null) {
        alertDialogMessage(requireContext(), "Masukkan Tanggal Registrasi dengan benar!")
        false
    } else if (viewModel.expirationDate.value == 0L || viewModel.expirationDate.value == null) {
        alertDialogMessage(requireContext(), "Masukkan Tanggal Expired dengan benar!")
        false
    } else {
        true
    }

    private fun clearAll() {
        binding.apply {
            edNama.setText("")
            edUmur.setText("")
            edHeight.setText("")
            edWeight.setText("")
            edTelp.setText("")
            edAddress.setText("")
            edRegistration.setText("")
            edExpration.setText("")
        }
    }
}