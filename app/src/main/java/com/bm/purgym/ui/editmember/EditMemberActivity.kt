package com.bm.purgym.ui.editmember

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bm.purgym.R
import com.bm.purgym.data.models.MemberModel
import com.bm.purgym.databinding.ActivityEditMemberBinding
import com.bm.purgym.utils.Constants
import com.bm.purgym.utils.Constants.alertDialogMessage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class EditMemberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditMemberBinding
    private val memberExtra: MemberModel? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_MEMBER, MemberModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_MEMBER)
        }
    }

    private val viewModel by viewModel<EditMemberViewModel>()
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri,
                    (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                )
                viewModel.uriImage.postValue(uri.toString())
                binding.ivProfile.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        memberExtra?.let {
            initViews(it)
            setListeners()
        }
    }

    private fun initViews(member: MemberModel) {
        binding.apply {
            viewModel.uriImage.postValue(member.profilePic)
            viewModel.registrationDate.postValue(member.registrationDate)
            viewModel.expirationDate.postValue(member.expirationDate)

            ivProfile.setImageURI(Uri.parse(member.profilePic))
            edNama.setText(member.name)
            if (member.gender == Constants.Gender.Male.name) {
                rbMale.isChecked = true
            } else {
                rbFemale.isChecked = true
            }
            edUmur.setText(member.age)
            edHeight.setText(member.height)
            edWeight.setText(member.weight)
            edTelp.setText(member.telp)
            edAddress.setText(member.address)
            edRegistration.setText(Constants.convertTimestamp(member.registrationDate))
            edExpration.setText(Constants.convertTimestamp(member.expirationDate))
        }
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
                    this@EditMemberActivity, { view, year, monthOfYear, dayOfMonth ->
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
                    this@EditMemberActivity, { view, year, monthOfYear, dayOfMonth ->
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

                    viewModel.editMember(memberModel)
                    alertDialogMessage(this@EditMemberActivity, "Member berhasil Diupdate!")
                    setResult(RESULT_OK)
                    finish()
                }
            }

            btnDelete.setOnClickListener {
                viewModel.deleteMember(memberExtra ?: MemberModel())
                alertDialogMessage(this@EditMemberActivity, "Member berhasil Dihapus!")
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun isValid() = if (viewModel.uriImage.value.isNullOrEmpty()) {
        alertDialogMessage(this, "Masukkan Foto Profil dengan benar!")
        false
    } else if (binding.edNama.text.isNullOrEmpty()) {
        alertDialogMessage(this, "Masukkan Nama dengan benar!")
        false
    } else if (binding.rgGender.checkedRadioButtonId == -1) {
        alertDialogMessage(this, "Pilih Gender dengan benar!")
        false
    } else if (binding.edUmur.text.isNullOrEmpty()) {
        alertDialogMessage(this, "Masukkan Umur dengan benar!")
        false
    } else if (binding.edHeight.text.isNullOrEmpty()) {
        alertDialogMessage(this, "Masukkan Tinggi Badan dengan benar!")
        false
    } else if (binding.edWeight.text.isNullOrEmpty()) {
        alertDialogMessage(this, "Masukkan Berat Badan dengan benar!")
        false
    } else if (binding.edTelp.text.isNullOrEmpty()) {
        alertDialogMessage(this, "Masukkan No. Telp dengan benar!")
        false
    } else if (binding.edAddress.text.isNullOrEmpty()) {
        alertDialogMessage(this, "Masukkan Alamat dengan benar!")
        false
    } else if (viewModel.registrationDate.value == 0L || viewModel.registrationDate.value == null) {
        alertDialogMessage(this, "Masukkan Tanggal Registrasi dengan benar!")
        false
    } else if (viewModel.expirationDate.value == 0L || viewModel.expirationDate.value == null) {
        alertDialogMessage(this, "Masukkan Tanggal Expired dengan benar!")
        false
    } else {
        true
    }

    companion object {
        const val EXTRA_MEMBER = "extra_member"
    }
}