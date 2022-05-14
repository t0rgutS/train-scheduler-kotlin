package ru.mirea.trainscheduler.repository.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.mirea.trainscheduler.model.Profile

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles WHERE code=:code")
    fun getProfileByCode(code: String): Profile?

    @Insert(onConflict = REPLACE)
    fun upsertProfile(profile: Profile)
}