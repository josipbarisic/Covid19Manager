package com.barisic.covid_19manager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.databinding.FragmentStanjePacijentaBinding
import com.barisic.covid_19manager.dialogs.StanjePacijentaDialogFragment
import com.barisic.covid_19manager.dialogs.TemperaturePickerDialogFragment
import com.barisic.covid_19manager.util.Common
import com.barisic.covid_19manager.viewmodels.StanjePacijentaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StanjePacijentaFragment : Fragment() {

    private lateinit var binding: FragmentStanjePacijentaBinding
    private val viewModel: StanjePacijentaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stanje_pacijenta, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = Common.getDate()

        binding.lifecycleOwner = this
        viewModel.lifecycleOwner = this
        viewModel.stateOnDayTitle = "${getString(R.string.state_title)} $date"

        val stanjePacijentaDialogFragment = StanjePacijentaDialogFragment(viewModel)
        val temperaturePickerDialogFragment = TemperaturePickerDialogFragment(viewModel)

        viewModel.showStanjePacijentaDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (!stanjePacijentaDialogFragment.isAdded) stanjePacijentaDialogFragment.show(
                    requireActivity().supportFragmentManager,
                    "StanjePacijentaDialogFragment"
                )
            } else {
                if (stanjePacijentaDialogFragment.isVisible) {
                    stanjePacijentaDialogFragment.dismiss()
                }
            }
        })
        viewModel.showTemperaturePickerDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (!temperaturePickerDialogFragment.isAdded) temperaturePickerDialogFragment.show(
                    requireActivity().supportFragmentManager,
                    "TemperaturePickerDialogFragment"
                )
            } else {
                if (temperaturePickerDialogFragment.isVisible) {
                    temperaturePickerDialogFragment.dismiss()
                }
            }
        })
        binding.stanjePacijentaViewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.showStanjePacijentaDialog.removeObservers(this)
        viewModel.showTemperaturePickerDialog.removeObservers(this)
        viewModel.showStanjePacijentaDialog.value = false
        viewModel.showTemperaturePickerDialog.value = false
    }
}