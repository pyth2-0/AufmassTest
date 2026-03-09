package com.aufmass.app.data.local.dao

import androidx.room.*
import com.aufmass.app.data.local.entity.RaumartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RaumartDao {
    @Query("SELECT * FROM einstellung_raumart ORDER BY bezeichnung")
    fun getAllRaumarten(): Flow<List<RaumartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(raumart: RaumartEntity): Long

    @Update
    suspend fun update(raumart: RaumartEntity)

    @Delete
    suspend fun delete(raumart: RaumartEntity)

    @Query("DELETE FROM einstellung_raumart")
    suspend fun deleteAll()
}
