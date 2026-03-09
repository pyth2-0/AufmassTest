package com.aufmass.app.data.repository

import com.aufmass.app.data.local.dao.GlasartDao
import com.aufmass.app.data.local.entity.GlasartEntity
import kotlinx.coroutines.flow.Flow

class GlasartRepository(private val glasartDao: GlasartDao) {
    fun getAllGlasarten(): Flow<List<GlasartEntity>> = glasartDao.getAllGlasarten()

    suspend fun insert(glasart: GlasartEntity): Long = glasartDao.insert(glasart)

    suspend fun update(glasart: GlasartEntity) = glasartDao.update(glasart)

    suspend fun delete(glasart: GlasartEntity) = glasartDao.delete(glasart)

    suspend fun deleteAll() = glasartDao.deleteAll()
}
