package com.barisic.covid_19manager.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.util.*
import com.barisic.covid_19manager.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private var doubleClickedBackToExit = false
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
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Timber.d(SharedPrefs(applicationContext).getValueString(LOGGED_USER_EPIDEMIOLOGIST))

        navController = findNavController(R.id.nav_host_fragment)
        bottomNavigation.setupWithNavController(navController)
        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.nav_info_fragment,
                R.id.nav_state_fragment,
                R.id.nav_state_history_fragment,
                R.id.nav_log_out_dialog
            )
        )
        setupActionBarWithNavController(navController, appBarConfig)

//        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationSelectedListener)
        if (savedInstanceState == null) {
            checkLocationPermissions()
//            bottomNavigation.selectedItemId = R.id.nav_state
        }

        mainViewModel.logOut.observe(this, logOutObserver)

        emergencyCallBtn.setOnClickListener {
            Common.makeEmergencyCall(this)
        }
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

    /*private val mOnNavigationSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_info -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.fast_fade_in, R.animator.fast_fade_out)
                        .addToBackStack(null)
                        .replace(R.id.frameLayout, infoFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_state -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.fast_fade_in, R.animator.fast_fade_out)
                        .addToBackStack(null)
                        .replace(R.id.frameLayout, stanjePacijentaFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_medical_records -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.fast_fade_in, R.animator.fast_fade_out)
                        .addToBackStack(null)
                        .replace(R.id.frameLayout, povijestStanjaFragment)
                        .commit()
                    //load states
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_logout -> {
                    showAlertDialog(
                        R.string.log_out_message,
                        R.string.log_oug,
                        {
                            SharedPrefs(applicationContext).save(LOGGED_USER, false)
                            SharedPrefs(applicationContext).removeValue(LOGGED_USER_ID)
                            SharedPrefs(applicationContext).removeValue(LOGGED_USER_LAT)
                            SharedPrefs(applicationContext).removeValue(LOGGED_USER_LONG)
                            SharedPrefs(applicationContext).removeValue(LOGGED_USER_OIB).also {
                                handleLoginPrefs()
                            }
                        },
                        R.string.cancel,
                        R.string.app_name
                    )
                    return@OnNavigationItemSelectedListener true
                }
                else -> return@OnNavigationItemSelectedListener false
            }
        }*/
}
