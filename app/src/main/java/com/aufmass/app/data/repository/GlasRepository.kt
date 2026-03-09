package com.aufmass.app.data.repository

import com.aufmass.app.data.local.dao.GlasDao
import com.aufmass.app.data.local.entity.GlasEntity
import kotlinx.coroutines.flow.Flow

class GlasRepository(private val glasDao: GlasDao) {
    fun getGlasByAufmassId(aufmassId: Long): Flow<List<GlasEntity>> = 
        glasDao.getGlasByAufmassId(aufmassId)

    suspend fun getGlasById(id: Long): GlasEntity? = glasDao.getGlasById(id)

    suspend fun insert(glas: GlasEntity): Long = glasDao.insert(glas)

    suspend fun update(glas: GlasEntity) = glasDao.update(glas)

    suspend fun delete(glas: GlasEntity) = glasDao.delete(glas)

    suspend fun deleteById(id: Long) = glasDao.deleteById(id)
}
