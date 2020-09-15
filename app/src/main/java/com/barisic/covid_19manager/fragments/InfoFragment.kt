package com.barisic.covid_19manager.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.databinding.FragmentInfoBinding
import com.barisic.covid_19manager.viewmodels.InfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    private val viewModel: InfoViewModel by viewModel()
    private var loadingControlValue = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        binding.infoViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingControlValue = false

        val webView = binding.webView
        webView.loadUrl("https://www.koronavirus.hr/")

        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.settings.useWideViewPort = true

        webView.canGoBack()
        webView.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK
                && event.action == MotionEvent.ACTION_UP
                && webView.canGoBack()
            ) {
                webView.goBack()
            }
            return@setOnKeyListener true
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                viewModel.loading.value = false
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return true
            }
        }
        webView.loadUrl("https://www.koronavirus.hr/")
    }


    override fun onPause() {
        super.onPause()
        viewModel.loading.value = true
    }
}