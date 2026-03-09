package com.aufmass.app.data.repository

import com.aufmass.app.data.local.dao.AufmassDao
import com.aufmass.app.data.local.entity.AufmassEntity
import kotlinx.coroutines.flow.Flow

class AufmassRepository(private val aufmassDao: AufmassDao) {
    fun getAllAufmass(): Flow<List<AufmassEntity>> = aufmassDao.getAllAufmass()

    suspend fun getAufmassById(id: Long): AufmassEntity? = aufmassDao.getAufmassById(id)

    suspend fun insert(aufmass: AufmassEntity): Long = aufmassDao.insert(aufmass)

    suspend fun update(aufmass: AufmassEntity) = aufmassDao.update(aufmass)

    suspend fun delete(aufmass: AufmassEntity) = aufmassDao.delete(aufmass)

    suspend fun deleteById(id: Long) = aufmassDao.deleteById(id)
}
