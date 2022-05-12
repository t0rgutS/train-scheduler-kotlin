package ru.mirea.trainscheduler.repository.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TrainSchedule : Serializable {
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null

    @SerializedName("interval_segments")
    @Expose
    var intervalSegments: List<IntervalSegment> = ArrayList()

    @SerializedName("segments")
    @Expose
    var segments: List<Segment> = ArrayList()

    @SerializedName("search")
    @Expose
    var search: Search? = null

    class Pagination : Serializable {
        @SerializedName("total")
        @Expose
        var total: Int? = null

        @SerializedName("limit")
        @Expose
        var limit: Int? = null

        @SerializedName("offset")
        @Expose
        var offset: Int? = null

        companion object {
            private const val serialVersionUID = 239808632234171446L
        }
    }

    class Search : Serializable {
        @SerializedName("date")
        @Expose
        var date: String? = null

        @SerializedName("to")
        @Expose
        var to: YandexApiStation? = null

        @SerializedName("from")
        @Expose
        var from: YandexApiStation? = null

        companion object {
            private const val serialVersionUID = -1861632636269192490L
        }
    }

    class IntervalSegment : Serializable {
        @SerializedName("from")
        @Expose
        var from: YandexApiStation? = null

        @SerializedName("thread")
        @Expose
        var thread: Thread? = null

        @SerializedName("departure_platform")
        @Expose
        var departurePlatform: String? = null

        @SerializedName("stops")
        @Expose
        var stops: String? = null

        @SerializedName("departure_terminal")
        @Expose
        var departureTerminal: Any? = null

        @SerializedName("to")
        @Expose
        var to: YandexApiStation? = null

        @SerializedName("has_transfers")
        @Expose
        var hasTransfers: Boolean? = null

        @SerializedName("tickets_info")
        @Expose
        var ticketsInfo: TicketsInfo? = null

        @SerializedName("duration")
        @Expose
        var duration: Int? = null

        @SerializedName("arrival_terminal")
        @Expose
        var arrivalTerminal: String? = null

        @SerializedName("start_date")
        @Expose
        var startDate: String? = null

        @SerializedName("arrival_platform")
        @Expose
        var arrivalPlatform: String? = null

        companion object {
            private const val serialVersionUID = 1720525721466029780L
        }
    }

    class Segment : Serializable {
        @SerializedName("arrival")
        @Expose
        var arrival: String? = null

        @SerializedName("from")
        @Expose
        var from: YandexApiStation? = null

        @SerializedName("thread")
        @Expose
        var thread: Thread? = null

        @SerializedName("departure_platform")
        @Expose
        var departurePlatform: String? = null

        @SerializedName("departure")
        @Expose
        var departure: String? = null

        @SerializedName("stops")
        @Expose
        var stops: String? = null

        @SerializedName("departure_terminal")
        @Expose
        var departureTerminal: Any? = null

        @SerializedName("to")
        @Expose
        var to: YandexApiStation? = null

        @SerializedName("has_transfers")
        @Expose
        var hasTransfers: Boolean? = null

        @SerializedName("tickets_info")
        @Expose
        var ticketsInfo: TicketsInfo? = null

        @SerializedName("duration")
        @Expose
        var duration: Int? = null

        @SerializedName("arrival_terminal")
        @Expose
        var arrivalTerminal: String? = null

        @SerializedName("start_date")
        @Expose
        var startDate: String? = null

        @SerializedName("arrival_platform")
        @Expose
        var arrivalPlatform: String? = null

        companion object {
            private const val serialVersionUID = 8786913111844287073L
        }
    }

    class TicketsInfo : Serializable {
        @SerializedName("et_marker")
        @Expose
        var etMarker: Boolean? = null

        @SerializedName("places")
        @Expose
        var places: List<Place> = ArrayList()

        companion object {
            private const val serialVersionUID = -2700140864244940145L
        }
    }

    class Place : Serializable {
        @SerializedName("currency")
        @Expose
        var currency: String? = null

        @SerializedName("price")
        @Expose
        var price: Price? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        companion object {
            private const val serialVersionUID = -6852888151356737057L
        }
    }

    class Price : Serializable {
        @SerializedName("cents")
        @Expose
        var cents: Int? = null

        @SerializedName("whole")
        @Expose
        var whole: Int? = null

        companion object {
            private const val serialVersionUID = 1552269788014783923L
        }
    }

    class Thread : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("number")
        @Expose
        var number: String? = null

        @SerializedName("short_title")
        @Expose
        var shortTitle: String? = null

        @SerializedName("thread_method_link")
        @Expose
        var threadMethodLink: String? = null

        @SerializedName("carrier")
        @Expose
        var carrier: Carrier? = null

        @SerializedName("transport_type")
        @Expose
        var transportType: String? = null

        @SerializedName("vehicle")
        @Expose
        var vehicle: String? = null

        @SerializedName("transport_subtype")
        @Expose
        var transportSubtype: TransportSubtype? = null

        @SerializedName("express_type")
        @Expose
        var expressType: Any? = null

        @SerializedName("interval")
        @Expose
        var interval: Interval? = null

        private val serialVersionUID = 6036654328482232632L

        companion object {
            private const val serialVersionUID = -6670191841898826571L
        }
    }

    class Carrier : Serializable {
        @SerializedName("code")
        @Expose
        var code: Int? = null

        @SerializedName("contacts")
        @Expose
        var contacts: String? = null

        @SerializedName("url")
        @Expose
        var url: String? = null

        @SerializedName("logo_svg")
        @Expose
        var logoSvg: Any? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("phone")
        @Expose
        var phone: String? = null

        @SerializedName("codes")
        @Expose
        var codes: Codes? = null

        @SerializedName("address")
        @Expose
        var address: String? = null

        @SerializedName("logo")
        @Expose
        var logo: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        companion object {
            private const val serialVersionUID = -7143238041205877968L
        }
    }

    class Codes : Serializable {
        @SerializedName("icao")
        @Expose
        var icao: Any? = null

        @SerializedName("sirena")
        @Expose
        var sirena: String? = null

        @SerializedName("iata")
        @Expose
        var iata: String? = null

        companion object {
            private const val serialVersionUID = -5286125496061144374L
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
            private const val serialVersionUID = -8793726233621261625L
        }
    }

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
            private const val serialVersionUID = 9217426425484938744L
        }
    }

    companion object {
        private const val serialVersionUID = 1072712016360844573L
    }
}