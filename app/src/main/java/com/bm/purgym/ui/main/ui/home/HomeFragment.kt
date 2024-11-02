package com.bm.purgym.ui.main.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bm.purgym.R
import com.bm.purgym.databinding.FragmentHomeBinding
import com.bm.purgym.ui.adapter.MemberAdapter
import com.bm.purgym.ui.editmember.EditMemberActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val memberAdapter = MemberAdapter()
    private val viewModel by viewModel<HomeViewModel>()

    val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            binding.rbAll.isChecked = true
            viewModel.getAllMembers()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        observeViewModel()
        setListeners()

        return root
    }

    private fun observeViewModel() {
        viewModel.apply {
            memberType.observe(viewLifecycleOwner) {
                when (it) {
                    R.id.rb_all -> {
                        viewModel.getAllMembers()
                    }

                    R.id.rb_active -> {
                        viewModel.getActiveMembers()
                    }

                    R.id.rb_unactive -> {
                        viewModel.getUnactiveMembers()
                    }
                }
            }

            memberList.observe(viewLifecycleOwner) {
                memberAdapter.notifyDataSetChanged()
                memberAdapter.submitList(it)
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            rgMemberType.setOnCheckedChangeListener { radioGroup, i ->
                svMember.setQuery("", false)
                viewModel.memberType.postValue(radioGroup.checkedRadioButtonId)
            }

            svMember.apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(keyword: String): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText.isNullOrEmpty()) {
                            viewModel.memberType.observe(viewLifecycleOwner) {
                                when (it) {
                                    R.id.rb_all -> {
                                        viewModel.getAllMembers()
                                    }

                                    R.id.rb_active -> {
                                        viewModel.getActiveMembers()
                                    }

                                    R.id.rb_unactive -> {
                                        viewModel.getUnactiveMembers()
                                    }
                                }
                            }
                        } else {
                            viewModel.searchMember(newText)
                        }
                        return false
                    }
                })
            }

            memberAdapter.onMemberClick = { item, _ ->
                val iEditMember = Intent(requireActivity(), EditMemberActivity::class.java)
                iEditMember.putExtra(EditMemberActivity.EXTRA_MEMBER, item)
                startForResult.launch(iEditMember)
            }

            rvMember.apply {
                adapter = memberAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
}