package com.barisic.covid_19manager.models

data class StanjePacijenta(
    val korisnikId: Long,
    val temperatura: Float,
    val kasalj: Int,
    val umor: Int,
    val bolUMisicima: Int,
    val vrijeme: Int
)