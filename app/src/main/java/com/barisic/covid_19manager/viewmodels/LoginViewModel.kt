package com.barisic.covid_19manager.viewmodels

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.models.Pacijent
import com.barisic.covid_19manager.repositories.EpidemiologRepository
import com.barisic.covid_19manager.repositories.JsonWebTokenRepository
import com.barisic.covid_19manager.repositories.PacijentRepository
import com.barisic.covid_19manager.util.*
import com.barisic.covid_19manager.util.Common.displayPopupErrorMessage
import com.google.gson.GsonBuilder

class LoginViewModel(
    var application: Application,
    private var pacijentRepository: PacijentRepository,
    private var epidemiologRepository: EpidemiologRepository,
    private var jsonTokenRepository: JsonWebTokenRepository
) :
    ViewModel() {
    var oibInputValidator = InputValidator(OIB)
    var idInputValidator = InputValidator(ID)
    lateinit var lifecycleOwner: LifecycleOwner

    private var jsonToken = MutableLiveData<String>()
    private var pacijent = MutableLiveData<String>()

    var errorMessage = MutableLiveData<Int?>()
    var loading = MutableLiveData<Int?>()
    var result = MutableLiveData<String?>()

    fun userLogin() {
        if (oibInputValidator.validateInput() && idInputValidator.validateInput()) {
            loading.value = LOADING
            jsonTokenRepository.getJsonToken(jsonToken)

            if (!jsonToken.hasObservers()) {
                jsonToken.observe(lifecycleOwner, Observer {
                    when (it) {
                        TOKEN_ACCESS_FAILED -> {
                            println("TOKEN RESPONSE ----> NULL")
                            displayPopupErrorMessage(
                                R.string.connection_error,
                                loading,
                                errorMessage
                            )
                        }
                        else -> pacijentRepository.getPacijent(
                            it,
                            oibInputValidator.inputValue.value.toString().toLong(),
                            pacijent
                        )
                    }
                })
            }

            if (!pacijent.hasObservers()) {
                pacijent.observe(lifecycleOwner, Observer {
                    if (pacijent.value != PACIENT_ACCESS_FAILED) {
                        println("PACIJENT CAPTURED -----------> ${pacijent.value.toString()}")
                        val pacijentObject = GsonBuilder().create()
                            .fromJson(pacijent.value.toString(), Pacijent::class.java)
                        if (pacijentObject.id != idInputValidator.inputValue.value.toString()
                                .toLong()
                        ) {
                            displayPopupErrorMessage(R.string.id_error, loading, errorMessage)
                        } else {
                            if (pacijentObject.status == 2) {
                                pacijentObject.status = 1
                                pacijentRepository.updatePacijent(
                                    pacijentObject.oib,
                                    pacijentObject
                                )
                            }
                            SharedPrefs(application.applicationContext).save(LOGGED_USER, true)
                            SharedPrefs(application.applicationContext).save(
                                LOGGED_USER_ID,
                                pacijentObject.id.toString()
                            )
                            SharedPrefs(application.applicationContext).save(
                                LOGGED_USER_OIB,
                                pacijentObject.oib.toString()
                            )
                            SharedPrefs(application.applicationContext).save(
                                LOGGED_USER_LAT,
                                pacijentObject.lat.toString()
                            )
                            SharedPrefs(application.applicationContext).save(
                                LOGGED_USER_LONG,
                                pacijentObject.long.toString()
                            )
                            epidemiologRepository.getAllEpidemiolozi(jsonToken.value!!) {
                                if (!it.isNullOrEmpty()) {
                                    SharedPrefs(application.applicationContext).save(
                                        LOGGED_USER_EPIDEMIOLOGIST,
                                        Common.getClosestEpidemiologist(
                                            pacijentObject.lat,
                                            pacijentObject.long,
                                            it
                                        )
                                    )
                                } else {
                                    SharedPrefs(application.applicationContext).save(
                                        LOGGED_USER_EPIDEMIOLOGIST,
                                        HZJZ_PHONE
                                    )
                                }
                            }
                            println("PACIJENT CAPTURED STATUS -----------> ${pacijentObject.status}")
                            result.value = RESULT_SUCCESS
                            loading.value = null
                        }
                    } else {
                        println("PACIJENT RESPONSE --------> NULL")
                        displayPopupErrorMessage(R.string.oib_error, loading, errorMessage)
                    }
                })
            }
        }
    }
}
