package ru.mirea.trainscheduler.repository.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class YandexApiStation {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("popular_title")
    @Expose
    var popularTitle: String? = null

    @SerializedName("short_title")
    @Expose
    var shortTitle: String? = null

    @SerializedName("transport_type")
    @Expose
    var transportType: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("station_type")
    @Expose
    var stationType: String? = null

    @SerializedName("station_type_name")
    @Expose
    var stationTypeName: String? = null

    @SerializedName("direction")
    @Expose
    var direction: String? = null

    @SerializedName("codes")
    @Expose
    var codes: Map<String, String>? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    companion object {
        private const val serialVersionUID = -4259920428993447977L
    }
}