package com.barisic.covid_19manager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.databinding.FragmentPovijestStanjaBinding
import com.barisic.covid_19manager.viewmodels.PovijestStanjaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PovijestStanjaFragment : Fragment() {

    private lateinit var binding: FragmentPovijestStanjaBinding
    private val viewModel: PovijestStanjaViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_povijest_stanja, container, false)
        binding.povijestStanjaViewModel = viewModel
//        binding.rvStanjaPacijenta.adapter =
        binding.lifecycleOwner = this
        return binding.root
    }
}