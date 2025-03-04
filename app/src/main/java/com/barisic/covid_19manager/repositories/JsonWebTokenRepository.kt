package com.barisic.covid_19manager.repositories

import androidx.lifecycle.MutableLiveData
import com.barisic.covid_19manager.interfaces.JSONWebTokenInterface
import com.barisic.covid_19manager.util.*
import com.google.gson.JsonObject
import okhttp3.FormBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JsonWebTokenRepository(private val jsonWebTokenService: JSONWebTokenInterface) {
    private var json: JsonObject? = null

    fun getJsonToken(token: MutableLiveData<String>) {
        jsonWebTokenService.getToken(
            FormBody.Builder()
                .add(
                    CLIENT_ID,
                    CLIENT_ID_VALUE
                )
                .add(
                    SCOPE,
                    SCOPE_VALUE
                )
                .add(
                    CLIENT_SECRET,
                    CLIENT_SECRET_VALUE
                )
                .add(
                    GRANT_TYPE,
                    GRANT_TYPE_VALUE
                )
                .build()
        ).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                println("FAILED TO GET TOKEN ----> ${t.message}")
                token.value = TOKEN_ACCESS_FAILED
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                println("TOKEN GRANTED ----> ${response.body()}")
                if (response.isSuccessful) {
                    json = response.body()
                    token.value = "bearer ${json!!["access_token"].asString}"
                }
            }
        })
    }

}