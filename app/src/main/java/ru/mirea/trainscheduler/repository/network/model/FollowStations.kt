package ru.mirea.trainscheduler.repository.network.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import ru.mirea.trainscheduler.repository.network.model.YandexApiStation
import java.io.Serializable
import java.util.ArrayList

class FollowStations : Serializable {
    @SerializedName("except_days")
    @Expose
    var exceptDays: String? = null

    @SerializedName("arrival_date")
    @Expose
    var arrivalDate: Any? = null

    @SerializedName("from")
    @Expose
    var from: YandexApiStation? = null

    @SerializedName("uid")
    @Expose
    var uid: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("interval")
    @Expose
    var interval: Interval? = null

    @SerializedName("departure_date")
    @Expose
    var departureDate: Any? = null

    @SerializedName("start_time")
    @Expose
    var startTime: String? = null

    @SerializedName("number")
    @Expose
    var number: String? = null

    @SerializedName("short_title")
    @Expose
    var shortTitle: String? = null

    @SerializedName("days")
    @Expose
    var days: String? = null

    @SerializedName("to")
    @Expose
    var to: YandexApiStation? = null

    @SerializedName("carrier")
    @Expose
    var carrier: Carrier? = null

    @SerializedName("transport_type")
    @Expose
    var transportType: String? = null

    @SerializedName("stops")
    @Expose
    var stops: List<Stop> = ArrayList()

    @SerializedName("vehicle")
    @Expose
    var vehicle: Any? = null

    @SerializedName("start_date")
    @Expose
    var startDate: String? = null

    @SerializedName("transport_subtype")
    @Expose
    var transportSubtype: TransportSubtype? = null

    @SerializedName("express_type")
    @Expose
    var expressType: Any? = null

    class Interval : Serializable {
        @SerializedName("density")
        @Expose
        var density: String? = null

        @SerializedName("end_time")
        @Expose
        var endTime: String? = null

        @SerializedName("begin_time")
        @Expose
        var beginTime: String? = null

        companion object {
            private const val serialVersionUID = 8171931030051147471L
        }
    }

    class Stop : Serializable {
        @SerializedName("arrival")
        @Expose
        var arrival: String? = null

        @SerializedName("departure")
        @Expose
        var departure: String? = null

        @SerializedName("terminal")
        @Expose
        var terminal: Any? = null

        @SerializedName("platform")
        @Expose
        var platform: String? = null

        @SerializedName("station")
        @Expose
        var station: YandexApiStation? = null

        @SerializedName("stop_time")
        @Expose
        var stopTime: Any? = null

        @SerializedName("duration")
        @Expose
        var duration: Double? = null

        companion object {
            private const val serialVersionUID = 8688478628627116488L
        }
    }

    class TransportSubtype : Serializable {
        @SerializedName("color")
        @Expose
        var color: String? = null

        @SerializedName("code")
        @Expose
        var code: String? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        companion object {
            private const val serialVersionUID = -8838791410902912131L
        }
    }

    class Carrier : Serializable {
        @SerializedName("code")
        @Expose
        var code: Int? = null

        @SerializedName("offices")
        @Expose
        var offices: List<Any> = ArrayList()

        @SerializedName("codes")
        @Expose
        var codes: Codes? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        companion object {
            private const val serialVersionUID = 4522729987437303562L
        }
    }

    class Codes : Serializable {
        @SerializedName("icao")
        @Expose
        var icao: Any? = null

        @SerializedName("sirena")
        @Expose
        var sirena: Any? = null

        @SerializedName("iata")
        @Expose
        var iata: Any? = null

        companion object {
            private const val serialVersionUID = 4009828535078000904L
        }
    }

    companion object {
        private const val serialVersionUID = -754816868377309495L
    }
}