package ru.mirea.trainscheduler.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
class Location {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Long? = null

    @ColumnInfo
    var city: String? = null

    @ColumnInfo
    var country: String? = null

    @ColumnInfo
    var region: String? = null

    @ColumnInfo(name = "city_codes")
    var codes: Map<String, String>? = HashMap()

    override fun toString(): String {
        val countryAndRegionPart: String = if (!country.isNullOrEmpty() && !region.isNullOrEmpty())
            "$country, $region" else if (country.isNullOrEmpty())
            if (!region.isNullOrEmpty()) region!! else ""
        else if (!country.isNullOrEmpty()) country!! else ""
        if (!city.isNullOrEmpty())
            return "$city ($countryAndRegionPart)"
        else
            return countryAndRegionPart
    }

    fun getDefaultCode(): String? {
        var code: String? = codes?.values?.first()
        if (code != null && !code.startsWith("c"))
            code = "c$code"
        return code
    }
}