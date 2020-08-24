package com.barisic.covid_19manager.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.databinding.ActivityMainBinding
import com.barisic.covid_19manager.util.*
import com.barisic.covid_19manager.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private var doubleClickedBackToExit = false

    private val callEpidemiologistObserver = Observer<Boolean> {
        if (it) {
            Common.makeEmergencyCall(this)
            viewModel.callEpidemiologist.value = false
        }
    }
    private val logOutObserver = Observer<Boolean> {
        if (it) {
            SharedPrefs(applicationContext).clearSharedPreference()
            SharedPrefs(applicationContext).save(LOGGED_USER, false).also {
                handleLoginPrefs()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this
        setSupportActionBar(toolbar)

        Timber.d(SharedPrefs(applicationContext).getValueString(LOGGED_USER_EPIDEMIOLOGIST_NUMBER))

        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.nav_info_fragment,
                R.id.nav_state_fragment,
                R.id.nav_state_history_fragment,
                R.id.nav_log_out_dialog
            )
        )
        setupActionBarWithNavController(navController, appBarConfig)

        if (savedInstanceState == null) {
            checkLocationPermissions()
        }

        viewModel.callEpidemiologist.observe(this, callEpidemiologistObserver)
        viewModel.logOut.observe(this, logOutObserver)
    }

    override fun onBackPressed() {
        if (doubleClickedBackToExit) {
            finish()
        } else {
            doubleClickedBackToExit = true
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({ doubleClickedBackToExit = false }, 2000)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Common.makeEmergencyCall(this)
            } else {
                Snackbar.make(
                    nav_host_fragment.requireView(),
                    getString(R.string.calling_not_enabled),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}
