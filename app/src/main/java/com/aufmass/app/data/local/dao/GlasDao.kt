package com.aufmass.app.data.local.dao

import androidx.room.*
import com.aufmass.app.data.local.entity.GlasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GlasDao {
    @Query("SELECT * FROM glas WHERE aufmassId = :aufmassId")
    fun getGlasByAufmassId(aufmassId: Long): Flow<List<GlasEntity>>

    @Query("SELECT * FROM glas WHERE id = :id")
    suspend fun getGlasById(id: Long): GlasEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(glas: GlasEntity): Long

    @Update
    suspend fun update(glas: GlasEntity)

    @Delete
    suspend fun delete(glas: GlasEntity)

    @Query("DELETE FROM glas WHERE id = :id")
    suspend fun deleteById(id: Long)
}
