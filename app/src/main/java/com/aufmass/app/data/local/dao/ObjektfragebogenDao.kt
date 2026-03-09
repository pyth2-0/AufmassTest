package com.aufmass.app.data.local.dao

import androidx.room.*
import com.aufmass.app.data.local.entity.ObjektfragebogenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjektfragebogenDao {
    @Query("SELECT * FROM objektfragebogen WHERE aufmassId = :aufmassId LIMIT 1")
    fun getByAufmassId(aufmassId: Long): Flow<ObjektfragebogenEntity?>

    @Query("SELECT * FROM objektfragebogen WHERE id = :id")
    suspend fun getById(id: Long): ObjektfragebogenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(objektfragebogen: ObjektfragebogenEntity)

    @Update
    suspend fun update(objektfragebogen: ObjektfragebogenEntity)

    @Delete
    suspend fun delete(objektfragebogen: ObjektfragebogenEntity)

    @Query("DELETE FROM objektfragebogen WHERE aufmassId = :aufmassId")
    suspend fun deleteByAufmassId(aufmassId: Long)
}
