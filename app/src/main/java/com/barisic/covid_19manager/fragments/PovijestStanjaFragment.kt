package com.barisic.covid_19manager.fragments

import android.os.Bundle
import android.view.*
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class PovijestStanjaFragment : Fragment() {

    private lateinit var binding: FragmentPovijestStanjaBinding
    private val viewModel: PovijestStanjaViewModel by viewModel()
    private lateinit var stanjaPacijentaAdapter: StanjaPacijentaAdapter

    private val povijestStanjaPacijentaObserver = Observer<ArrayList<StanjePacijenta>> {
        stanjaPacijentaAdapter = StanjaPacijentaAdapter(it)
        stanjaPacijentaAdapter.notifyDataSetChanged().apply {
            setupRecyclerView()
        }
        setHasOptionsMenu(true)
    }

    private val errorMessageObserver = Observer<Int?> {
        it?.let {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.povijest_stanja_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        menu.findItem(R.id.nav_reverse_order).setOnMenuItemClickListener {
            stanjaPacijentaAdapter.reverseListOrder()
            return@setOnMenuItemClickListener true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_povijest_stanja, container, false)
        if (viewModel.loading.value == null || viewModel.loading.value == false)
            viewModel.getStanjaPacijenta()
        viewModel.povijestStanjaPacijenta.observe(
            viewLifecycleOwner,
            povijestStanjaPacijentaObserver
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.povijestStanjaViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.errorMessage.observe(viewLifecycleOwner, errorMessageObserver)
    }

    private fun setupRecyclerView() {
        binding.rvStanjaPacijenta.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStanjaPacijenta.adapter = stanjaPacijentaAdapter
    }
}