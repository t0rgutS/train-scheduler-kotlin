package ru.mirea.trainscheduler.repository.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations WHERE city = :searchBy")
    fun findLocation(searchBy: String): List<Location>

    @Query("SELECT * FROM locations WHERE city LIKE :suggestBy || '%'")
    fun suggestLocations(suggestBy: String): Flow<List<Location>>

    @Query("SELECT COUNT(*) FROM locations")
    fun countLocations(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLocationList(locationList: List<Location>)

    @Query("DELETE FROM locations")
    fun clearLocations()
}