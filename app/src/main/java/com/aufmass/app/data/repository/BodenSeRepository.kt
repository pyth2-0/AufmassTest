package com.aufmass.app.data.repository

import com.aufmass.app.data.local.dao.BodenSeDao
import com.aufmass.app.data.local.entity.BodenSeEntity
import kotlinx.coroutines.flow.Flow

class BodenSeRepository(private val bodenSeDao: BodenSeDao) {
    fun getBodenSeByAufmassId(aufmassId: Long): Flow<List<BodenSeEntity>> = 
        bodenSeDao.getBodenSeByAufmassId(aufmassId)

    suspend fun getBodenSeById(id: Long): BodenSeEntity? = bodenSeDao.getBodenSeById(id)

    suspend fun insert(bodenSe: BodenSeEntity): Long = bodenSeDao.insert(bodenSe)

    suspend fun update(bodenSe: BodenSeEntity) = bodenSeDao.update(bodenSe)

    suspend fun delete(bodenSe: BodenSeEntity) = bodenSeDao.delete(bodenSe)

    suspend fun deleteById(id: Long) = bodenSeDao.deleteById(id)
}
