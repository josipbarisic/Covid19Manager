package com.barisic.covid_19manager.models

data class Epidemiolog(
    val id: Long,
    val zzjz: String,
    val lat: Float,
    val long: Float,
    var brojTelefona: String
)