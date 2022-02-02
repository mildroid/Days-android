package com.mildroid.days.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mildroid.days.domain.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEvents(events: List<Event>)

    @Query("SELECT * FROM events")
    fun events(): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun eventById(id: Int): Event
}