package com.barisic.covid_19manager.util

import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout

object BindingAdapter {
    @BindingAdapter("setErrorMessage")
    @JvmStatic
    fun setErrorMessage(textInputLayout: TextInputLayout, resource: Int?) {
        when (resource != null) {
            true -> {
                textInputLayout.error = textInputLayout.context.getString(resource)
                textInputLayout.requestFocus()
            }
            else -> textInputLayout.error = null
        }
        textInputLayout.editText!!.addTextChangedListener {
            when (it!!.isNotEmpty()) {
                true -> textInputLayout.error = null
            }
        }
    }

    @BindingAdapter("setPopupErrorMessage")
    @JvmStatic
    fun setPopupErrorMessage(textInputLayout: TextInputLayout, resource: Int?) {
        when (resource != null) {
            true -> {
                if (!textInputLayout.isVisible) {
                    textInputLayout.visibility = View.VISIBLE
                    textInputLayout.error = textInputLayout.context.getString(resource)
                }
            }
            false -> {
                if (textInputLayout.isVisible) {
                    textInputLayout.visibility = View.GONE
                }
            }
        }
    }

    @BindingAdapter(
        value = ["firstNPTemperatureValue", "firstNPIntegerNumber", "firstNPDecimalNumber"],
        requireAll = true
    )
    @JvmStatic
    fun npTempIntegerValue(
        numberPicker: NumberPicker,
        temperatureValue: MutableLiveData<String>,
        integerNumber: MutableLiveData<Int>,
        decimalNumber: MutableLiveData<Int>
    ) {
        numberPicker.maxValue = 43
        numberPicker.minValue = 33
        numberPicker.value = integerNumber.value!!
        numberPicker.setOnValueChangedListener { picker, _, _ ->
            integerNumber.value = picker.value
            temperatureValue.value =
                Common.combineIntegersToFloatString(picker.value, decimalNumber.value!!)
        }
    }

    @BindingAdapter(
        value = ["secondNPTemperatureValue", "secondNPIntegerNumber", "secondNPDecimalNumber"],
        requireAll = true
    )
    @JvmStatic
    fun npTempDecimalValue(
        numberPicker: NumberPicker,
        temperatureValue: MutableLiveData<String>,
        integerNumber: MutableLiveData<Int>,
        decimalNumber: MutableLiveData<Int>
    ) {
        numberPicker.maxValue = 9
        numberPicker.minValue = 0
        numberPicker.value = decimalNumber.value!!
        numberPicker.setOnValueChangedListener { picker, _, _ ->
            decimalNumber.value = picker.value
            temperatureValue.value =
                Common.combineIntegersToFloatString(integerNumber.value!!, picker.value)
        }
    }

    @BindingAdapter("switchValue")
    @JvmStatic
    fun switchValue(switch: SwitchCompat, switchValue: MutableLiveData<Boolean>) {
        switch.setOnCheckedChangeListener { _, isChecked ->

                switchValue.value = isChecked

        }
    }
}