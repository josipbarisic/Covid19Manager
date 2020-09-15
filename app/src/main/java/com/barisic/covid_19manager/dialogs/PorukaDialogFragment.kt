package com.barisic.covid_19manager.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.databinding.DialogFragmentPorukaBinding
import com.barisic.covid_19manager.viewmodels.PorukaViewModel

class PorukaDialogFragment(private val viewModel: PorukaViewModel) : DialogFragment() {
    private lateinit var dataBinding: DialogFragmentPorukaBinding

    private val responseObserver = Observer<Int?> {
        it?.let {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            viewModel.response.value = null
        }
    }
    private val closeDialogObserver = Observer<Boolean> {
        if (it) {
            this.dismiss()
            viewModel.closeDialog.value = false
        }
    }
    private val onEditorActionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        return@OnEditorActionListener when (actionId) {
            EditorInfo.IME_ACTION_SEND -> {
                viewModel.sendMessage()
                true
            }
            else -> false
        }
    }

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
            R.layout.dialog_fragment_poruka,
            container,
            false
        )
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.porukaViewModel = viewModel

        viewModel.lifecycleOwner = viewLifecycleOwner
        viewModel.response.observe(viewLifecycleOwner, responseObserver)
        viewModel.closeDialog.observe(viewLifecycleOwner, closeDialogObserver)
        dataBinding.etPoruka.setOnEditorActionListener(onEditorActionListener)

        return dataBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.response.removeObservers(dataBinding.lifecycleOwner!!)
        viewModel.closeDialog.removeObservers(dataBinding.lifecycleOwner!!)
    }
}