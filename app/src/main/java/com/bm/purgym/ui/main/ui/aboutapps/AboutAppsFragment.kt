package com.bm.purgym.ui.main.ui.aboutapps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bm.purgym.databinding.FragmentAboutAppsBinding

class AboutAppsFragment : Fragment() {

    private var _binding: FragmentAboutAppsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
}