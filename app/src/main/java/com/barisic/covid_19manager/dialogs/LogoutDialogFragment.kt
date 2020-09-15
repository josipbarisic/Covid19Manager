package com.barisic.covid_19manager.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.databinding.DialogFragmentLogOutBinding
import com.barisic.covid_19manager.viewmodels.MainViewModel

class LogoutDialogFragment(private val mainViewModel: MainViewModel) : DialogFragment() {
    private lateinit var binding: DialogFragmentLogOutBinding

    private val cancelLogOutObserver = Observer<Boolean> {
        if (it) {
            dismiss()
            mainViewModel.cancelLogOut.value = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.WhiteDialogBackgroundStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_log_out, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.cancelLogOut.observe(viewLifecycleOwner, cancelLogOutObserver)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}