package ru.mirea.trainscheduler.repository.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CurrencyCodes : Serializable {
    var result: String? = null
    var documentation: String? = null

    @SerializedName("terms_of_use")
    var termsOfUse: String? = null

    @SerializedName("supported_codes")
    var supportedCodes: List<List<String>> = ArrayList()
    var additionalProperties: Map<String, Any> = HashMap()

    companion object {
        private const val serialVersionUID = 780753646117378306L
    }
}