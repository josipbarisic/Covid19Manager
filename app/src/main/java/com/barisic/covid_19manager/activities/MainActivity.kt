package com.barisic.covid_19manager.activities

import android.content.pm.PackageManager
import android.os.Bundle
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.fragments.InfoFragment
import com.barisic.covid_19manager.fragments.PovijestStanjaFragment
import com.barisic.covid_19manager.fragments.StanjePacijentaFragment
import com.barisic.covid_19manager.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var infoFragment: InfoFragment
    private lateinit var stanjePacijentaFragment: StanjePacijentaFragment
    private lateinit var povijestStanjaFragment: PovijestStanjaFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        infoFragment = InfoFragment()
        stanjePacijentaFragment = StanjePacijentaFragment()
        povijestStanjaFragment = PovijestStanjaFragment()

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationSelectedListener)
        if (savedInstanceState == null) {
            checkLocationPermissions()
            bottomNavigation.selectedItemId = R.id.nav_state
        }

        emergencyCallBtn.setOnClickListener {
            Common.makeEmergencyCall(this)
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
                    frameLayout,
                    getString(R.string.calling_not_enabled),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val mOnNavigationSelectedListener =
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
        }
}
