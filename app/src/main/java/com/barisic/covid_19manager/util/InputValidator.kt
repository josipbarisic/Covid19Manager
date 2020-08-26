package com.barisic.covid_19manager.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.barisic.covid_19manager.R

class InputValidator(private var inputType: String?) {
    val inputValue = MutableLiveData<String>()
    private val displayedInputValue: LiveData<String>
        get() = inputValue
    val errorMsg = MutableLiveData<Int?>()

    fun validateInput(): Boolean {
        if (displayedInputValue.value.isNullOrEmpty()) {
            errorMsg.value = R.string.mandatory_field_error
            println("INPUT_ERROR --> EMPTY")
            return false
        }
        when (inputType) {
            OIB -> {
                if (displayedInputValue.value.toString().length < 11) {
                    errorMsg.value = R.string.eleven_error
                    println("OIB_INPUT_ERROR --> TOO SHORT")
                    return false
                }
                println("<-------VALIDATION PASSED------->")
                return true
            }
            ID -> {
                if (displayedInputValue.value.toString().length < 4) {
                    errorMsg.value = R.string.four_error
                    println("ID_INPUT_ERROR --> TOO SHORT")
                    return false
                }
                println("<-------VALIDATION PASSED------->")
                return true
            }
            MESSAGE -> {
                if (displayedInputValue.value!!.trim().isEmpty()) {
                    errorMsg.value = R.string.mandatory_field_error
                    return false
                }
                return true
            }
        }
        return false
    }
}