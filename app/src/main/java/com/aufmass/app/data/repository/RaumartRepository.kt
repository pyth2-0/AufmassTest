package com.aufmass.app.data.repository

import com.aufmass.app.data.local.dao.RaumartDao
import com.aufmass.app.data.local.entity.RaumartEntity
import kotlinx.coroutines.flow.Flow

class RaumartRepository(private val raumartDao: RaumartDao) {
    fun getAllRaumarten(): Flow<List<RaumartEntity>> = raumartDao.getAllRaumarten()

    suspend fun insert(raumart: RaumartEntity): Long = raumartDao.insert(raumart)

    suspend fun update(raumart: RaumartEntity) = raumartDao.update(raumart)

    suspend fun delete(raumart: RaumartEntity) = raumartDao.delete(raumart)

    suspend fun deleteAll() = raumartDao.deleteAll()
}
