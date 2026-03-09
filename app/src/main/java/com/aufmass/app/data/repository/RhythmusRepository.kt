package com.aufmass.app.data.repository

import com.aufmass.app.data.local.dao.RhythmusDao
import com.aufmass.app.data.local.entity.RhythmusEntity
import kotlinx.coroutines.flow.Flow

class RhythmusRepository(private val rhythmusDao: RhythmusDao) {
    fun getAllRhythmen(): Flow<List<RhythmusEntity>> = rhythmusDao.getAllRhythmen()

    suspend fun insert(rhythmus: RhythmusEntity): Long = rhythmusDao.insert(rhythmus)

    suspend fun update(rhythmus: RhythmusEntity) = rhythmusDao.update(rhythmus)

    suspend fun delete(rhythmus: RhythmusEntity) = rhythmusDao.delete(rhythmus)

    suspend fun deleteAll() = rhythmusDao.deleteAll()
}
