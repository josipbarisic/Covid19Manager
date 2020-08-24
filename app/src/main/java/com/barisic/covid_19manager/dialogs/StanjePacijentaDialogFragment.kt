package com.barisic.covid_19manager.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.databinding.DialogFragmentStanjePacijentaBinding
import com.barisic.covid_19manager.viewmodels.StanjePacijentaViewModel

class StanjePacijentaDialogFragment(var viewModel: StanjePacijentaViewModel) : DialogFragment() {

    private lateinit var dataBinding: DialogFragmentStanjePacijentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.WhiteDialogBackgroundStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_fragment_stanje_pacijenta,
            container,
            false
        )
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.stanjePacijentaViewModel = viewModel
        return dataBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.loading.removeObservers(dataBinding.lifecycleOwner!!)
    }
}