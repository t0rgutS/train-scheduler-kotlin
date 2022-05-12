package ru.mirea.trainscheduler.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
class Currency {
    @PrimaryKey
    @NonNull
    var code: String? = null

    constructor(code: String) {
        this.code = code
    }
}