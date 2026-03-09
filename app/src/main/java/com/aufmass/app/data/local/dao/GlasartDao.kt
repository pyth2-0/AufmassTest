package com.aufmass.app.data.local.dao

import androidx.room.*
import com.aufmass.app.data.local.entity.GlasartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GlasartDao {
    @Query("SELECT * FROM einstellung_glasart ORDER BY bezeichnung")
    fun getAllGlasarten(): Flow<List<GlasartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(glasart: GlasartEntity): Long

    @Update
    suspend fun update(glasart: GlasartEntity)

    @Delete
    suspend fun delete(glasart: GlasartEntity)

    @Query("DELETE FROM einstellung_glasart")
    suspend fun deleteAll()
}
