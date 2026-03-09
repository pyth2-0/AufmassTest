package com.aufmass.app.data.local.dao

import androidx.room.*
import com.aufmass.app.data.local.entity.RhythmusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RhythmusDao {
    @Query("SELECT * FROM einstellung_rhythmus ORDER BY exportwert")
    fun getAllRhythmen(): Flow<List<RhythmusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rhythmus: RhythmusEntity): Long

    @Update
    suspend fun update(rhythmus: RhythmusEntity)

    @Delete
    suspend fun delete(rhythmus: RhythmusEntity)

    @Query("DELETE FROM einstellung_rhythmus")
    suspend fun deleteAll()
}
