package com.barisic.covid_19manager.models

data class Poruka(
    val posiljatelj: Long,
    val vrijeme: Long,
    val text_poruke: String
)