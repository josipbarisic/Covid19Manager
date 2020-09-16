package com.barisic.covid_19manager.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
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
import com.barisic.covid_19manager.dialogs.LogoutDialogFragment
import com.barisic.covid_19manager.dialogs.PorukaDialogFragment
import com.barisic.covid_19manager.util.*
import com.barisic.covid_19manager.viewmodels.MainViewModel
import com.barisic.covid_19manager.viewmodels.PorukaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private val porukaViewModel: PorukaViewModel by viewModel()
    private lateinit var logOutDialog: LogoutDialogFragment
    private lateinit var porukaDialogFragment: PorukaDialogFragment
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.message_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_send_message -> porukaDialogFragment.show(
                supportFragmentManager,
                "PorukaDialogFragment"
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this
        setSupportActionBar(toolbar)

        logOutDialog = LogoutDialogFragment(viewModel)
        porukaDialogFragment = PorukaDialogFragment(porukaViewModel)

        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.menu.getItem(4).setOnMenuItemClickListener {
            if (it.itemId == R.id.nav_log_out_dialog) {
                Timber.d("CLICKED LOG_OUT")
                logOutDialog.show(supportFragmentManager, "LogOutDialogFragment")
            }
            return@setOnMenuItemClickListener false
        }

        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.nav_info_fragment,
                R.id.nav_state_fragment,
                R.id.nav_state_history_fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfig)

        if (savedInstanceState == null) {
            checkLocationPermissions()
        }

        when (intent.action) {
            ACTION_OPEN_STANJE_PACIJENTA -> bottomNavigation.selectedItemId =
                R.id.nav_state_fragment
        }

        viewModel.callEpidemiologist.observe(this, callEpidemiologistObserver)
        viewModel.logOut.observe(this, logOutObserver)

        viewModel.enqueueWork()

        Timber.d(
            "WORKER RUNNING -> ${
                !viewModel.workManager
                    .getWorkInfosByTag(STANJE_PACIJENTA_UPWT).isCancelled
            }"
        )
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
