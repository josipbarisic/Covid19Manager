package com.barisic.covid_19manager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.adapters.StanjaPacijentaAdapter
import com.barisic.covid_19manager.databinding.FragmentPovijestStanjaBinding
import com.barisic.covid_19manager.models.StanjePacijenta
import com.barisic.covid_19manager.viewmodels.PovijestStanjaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_povijest_stanja.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class PovijestStanjaFragment : Fragment() {

    private lateinit var binding: FragmentPovijestStanjaBinding
    private val viewModel: PovijestStanjaViewModel by viewModel()
    private lateinit var stanjaPacijentaAdapter: StanjaPacijentaAdapter

    private val povijestStanjaPacijentaObserver = Observer<ArrayList<StanjePacijenta>> {
        it.forEach { stanjePacijenta ->
            Timber.d("${stanjePacijenta.korisnikId} ${stanjePacijenta.vrijeme} ${stanjePacijenta.temperatura}")
        }
        stanjaPacijentaAdapter = StanjaPacijentaAdapter(it)
        binding.rvStanjaPacijenta.adapter = stanjaPacijentaAdapter
        stanjaPacijentaAdapter.notifyDataSetChanged()
    }

    private val errorMessageObserver = Observer<Int?> {
        it?.let {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_povijest_stanja, container, false)
        viewModel.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.povijestStanjaViewModel = viewModel
        binding.lifecycleOwner = this

        rvStanjaPacijenta.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getStanjaPacijenta()

        viewModel.povijestStanjaPacijenta.observe(
            viewLifecycleOwner,
            povijestStanjaPacijentaObserver
        )
        viewModel.errorMessage.observe(viewLifecycleOwner, errorMessageObserver)
    }
}