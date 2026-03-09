package com.aufmass.app.data.repository

import com.aufmass.app.data.local.dao.RaumDao
import com.aufmass.app.data.local.entity.RaumEntity
import kotlinx.coroutines.flow.Flow

class RaumRepository(private val raumDao: RaumDao) {
    fun getRaeumeByAufmassId(aufmassId: Long): Flow<List<RaumEntity>> = 
        raumDao.getRaeumeByAufmassId(aufmassId)

    suspend fun getRaumById(id: Long): RaumEntity? = raumDao.getRaumById(id)

    suspend fun insert(raum: RaumEntity): Long = raumDao.insert(raum)

    suspend fun update(raum: RaumEntity) = raumDao.update(raum)

    suspend fun delete(raum: RaumEntity) = raumDao.delete(raum)

    suspend fun deleteById(id: Long) = raumDao.deleteById(id)
}
