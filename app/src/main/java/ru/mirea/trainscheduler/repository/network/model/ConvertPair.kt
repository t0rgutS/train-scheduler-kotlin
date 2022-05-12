package ru.mirea.trainscheduler.repository.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.HashMap

class ConvertPair : Serializable {
    var result: String? = null
    var documentation: String? = null

    @SerializedName("terms_of_use")
    var termsOfUse: String? = null

    @SerializedName("time_last_update_unix")
    var timeLastUpdateUnix: Long? = null

    @SerializedName("time_last_update_utc")
    var timeLastUpdateUtc: String? = null

    @SerializedName("time_next_update_unix")
    var timeNextUpdateUnix: Long? = null

    @SerializedName("time_next_update_utc")
    var timeNextUpdateUtc: String? = null

    @SerializedName("base_code")
    var baseCode: String? = null

    @SerializedName("target_code")
    var targetCode: String? = null

    @SerializedName("conversion_rate")
    var conversionRate: Double? = null
    private val additionalProperties: Map<String, Any> = HashMap()

    companion object {
        private const val serialVersionUID = -3827350485945141391L
    }
}