package com.barisic.covid_19manager.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.repositories.EpidemiologRepository
import com.barisic.covid_19manager.repositories.PacijentRepository
import com.barisic.covid_19manager.util.*
import com.barisic.covid_19manager.util.Common.displayPopupErrorMessage

class LoginViewModel(
    private var context: Context,
    private var pacijentRepository: PacijentRepository,
    private var epidemiologRepository: EpidemiologRepository
) :
    ViewModel() {
    var oibInputValidator = InputValidator(OIB)
    var idInputValidator = InputValidator(ID)

    private val sharedPrefs = SharedPrefs(context)

    var errorMessage = MutableLiveData<Int?>()
    var loading = MutableLiveData(false)
    var result = MutableLiveData(false)

    fun userLogin() {
        if (oibInputValidator.validateInput() && idInputValidator.validateInput()) {
            loading.value = true

            pacijentRepository.getPacijent(
                oibInputValidator.inputValue.value.toString().toLong()
            ) { pacijent, from ->
                when (pacijent) {
                    null -> {
                        when (from) {
                            ON_RESPONSE -> displayPopupErrorMessage(
                                R.string.oib_error,
                                loading,
                                errorMessage
                            )
                            else -> displayPopupErrorMessage(
                                R.string.connection_error,
                                loading,
                                errorMessage
                            )
                        }
                    }
                    else -> {
                        if (pacijent.id != idInputValidator.inputValue.value.toString()
                                .toLong()
                        ) {
                            displayPopupErrorMessage(
                                R.string.id_error,
                                loading,
                                errorMessage
                            )
                        } else {
                            if (pacijent.status == 2) {
                                pacijent.status = 1
                                pacijentRepository.updatePacijent(
                                    pacijent.oib,
                                    pacijent
                                )
                            }
                            sharedPrefs.save(LOGGED_USER, true)
                            sharedPrefs.save(LOGGED_USER_ID, pacijent.id.toString())
                            sharedPrefs.save(LOGGED_USER_OIB, pacijent.oib.toString())
                            sharedPrefs.save(LOGGED_USER_LAT, pacijent.lat.toString())
                            sharedPrefs.save(LOGGED_USER_LONG, pacijent.long.toString())

                            epidemiologRepository.getAllEpidemiolozi {
                                if (!it.isNullOrEmpty()) {
                                    sharedPrefs.save(
                                        LOGGED_USER_EPIDEMIOLOGIST_NUMBER,
                                        Common.getClosestEpidemiologist(
                                            context,
                                            pacijent.lat,
                                            pacijent.long,
                                            it
                                        )
                                    )
                                } else {
                                    sharedPrefs.save(
                                        LOGGED_USER_EPIDEMIOLOGIST_ZZJZ,
                                        HZJZ_NAME
                                    )
                                    sharedPrefs.save(
                                        LOGGED_USER_EPIDEMIOLOGIST_NUMBER,
                                        HZJZ_PHONE
                                    )
                                }
                            }
                            result.value = true
                        }
                    }
                }
                loading.value = false
            }
        }
    }
}
