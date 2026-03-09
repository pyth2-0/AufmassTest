package com.aufmass.app.data.repository

import com.aufmass.app.data.local.dao.BodenbelagDao
import com.aufmass.app.data.local.entity.BodenbelagEntity
import kotlinx.coroutines.flow.Flow

class BodenbelagRepository(private val bodenbelagDao: BodenbelagDao) {
    fun getAllBodenbelage(): Flow<List<BodenbelagEntity>> = bodenbelagDao.getAllBodenbelage()

    suspend fun insert(bodenbelag: BodenbelagEntity): Long = bodenbelagDao.insert(bodenbelag)

    suspend fun update(bodenbelag: BodenbelagEntity) = bodenbelagDao.update(bodenbelag)

    suspend fun delete(bodenbelag: BodenbelagEntity) = bodenbelagDao.delete(bodenbelag)

    suspend fun deleteAll() = bodenbelagDao.deleteAll()
}
