package ru.mirea.trainscheduler.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "currency_exchange", primaryKeys = ["source", "target"])
class CurrencyExchange() {
    @ColumnInfo
    @NonNull
    var source: String? = null

    @ColumnInfo
    @NonNull
    var target: String? = null

    @ColumnInfo
    var rate: Double? = null

    @ColumnInfo(name = "last_modified")
    var lastModifiedOn: Long? = null

    @ColumnInfo(name = "next_update")
    var nextUpdateOn: Long? = null
}