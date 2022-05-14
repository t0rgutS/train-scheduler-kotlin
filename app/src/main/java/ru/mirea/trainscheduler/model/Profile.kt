package ru.mirea.trainscheduler.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
class Profile {
    @PrimaryKey
    @NonNull
    var code: String? = null

    @ColumnInfo
    @NonNull
    var value: String? = null

    enum class ThemeProfileValues { DARK, DEFAULT }
}