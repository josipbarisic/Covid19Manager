package com.barisic.covid_19manager.models

import com.barisic.covid_19manager.util.Common

data class StanjePacijenta(
    val korisnikId: Long,
    val temperatura: Float,
    val kasalj: Int,
    val umor: Int,
    val bolUMisicima: Int,
    val vrijeme: Long
) {
    fun getKorisnikIdString(): String {
        return korisnikId.toString()
    }

    fun getTemperaturaString(): String {
        return temperatura.toString()
    }

    fun getKasaljString(): String {
        return Common.getStringFromInt(kasalj)
    }

    fun getUmorString(): String {
        return Common.getStringFromInt(umor)
    }

    fun getBolUMisicimaString(): String {
        return Common.getStringFromInt(bolUMisicima)
    }

    fun getVrijemeString(): String {
        return Common.parseLongDateToString(vrijeme)
    }

    fun getVrijemeString2(): String {
        return vrijeme.toString()
    }
}