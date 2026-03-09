package com.aufmass.app.data.local.dao

import androidx.room.*
import com.aufmass.app.data.local.entity.LvEinstellungEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LvEinstellungDao {
    @Query("SELECT * FROM einstellung_lv ORDER BY raumart, spalte")
    fun getAllLvEinstellungen(): Flow<List<LvEinstellungEntity>>

    @Query("SELECT * FROM einstellung_lv WHERE raumart = :raumart ORDER BY spalte")
    fun getLvEinstellungenByRaumart(raumart: String): Flow<List<LvEinstellungEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lv: LvEinstellungEntity): Long

    @Update
    suspend fun update(lv: LvEinstellungEntity)

    @Delete
    suspend fun delete(lv: LvEinstellungEntity)

    @Query("DELETE FROM einstellung_lv")
    suspend fun deleteAll()
}
