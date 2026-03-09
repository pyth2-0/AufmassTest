package com.aufmass.app.data.repository

import com.aufmass.app.data.local.dao.LvEinstellungDao
import com.aufmass.app.data.local.entity.LvEinstellungEntity
import kotlinx.coroutines.flow.Flow

class LvEinstellungRepository(private val lvEinstellungDao: LvEinstellungDao) {
    fun getAllLvEinstellungen(): Flow<List<LvEinstellungEntity>> = 
        lvEinstellungDao.getAllLvEinstellungen()

    fun getLvEinstellungenByRaumart(raumart: String): Flow<List<LvEinstellungEntity>> = 
        lvEinstellungDao.getLvEinstellungenByRaumart(raumart)

    suspend fun insert(lv: LvEinstellungEntity): Long = lvEinstellungDao.insert(lv)

    suspend fun update(lv: LvEinstellungEntity) = lvEinstellungDao.update(lv)

    suspend fun delete(lv: LvEinstellungEntity) = lvEinstellungDao.delete(lv)

    suspend fun deleteAll() = lvEinstellungDao.deleteAll()
}
