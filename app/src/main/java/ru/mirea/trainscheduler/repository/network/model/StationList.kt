package ru.mirea.trainscheduler.repository.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StationList : Serializable {
    @SerializedName("countries")
    @Expose
    var countries: List<Country> = ArrayList()

    class Country : Serializable {
        @SerializedName("regions")
        @Expose
        var regions: List<Region> = ArrayList()

        @SerializedName("codes")
        @Expose
        var codes: Map<String, String>? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        companion object {
            private const val serialVersionUID = -8157184190911770021L
        }
    }

    class Region : Serializable {
        @SerializedName("settlements")
        @Expose
        var settlements: List<Settlement> = ArrayList()

        @SerializedName("codes")
        @Expose
        var codes: Map<String, String>? = HashMap()

        @SerializedName("title")
        @Expose
        var title: String? = null

        companion object {
            private const val serialVersionUID = -8696985401115516237L
        }
    }

    class Settlement : Serializable {
        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("codes")
        @Expose
        var codes: Map<String, String>? = null

        @SerializedName("stations")
        @Expose
        var stations: List<YandexApiStation> = ArrayList()

        companion object {
            private const val serialVersionUID = -3598852276384752282L
        }
    }

    companion object {
        private const val serialVersionUID = 4714133644101414103L
    }
}