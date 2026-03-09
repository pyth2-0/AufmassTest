package com.aufmass.app.data.repository

import com.aufmass.app.data.local.dao.ObjektfragebogenDao
import com.aufmass.app.data.local.entity.ObjektfragebogenEntity
import kotlinx.coroutines.flow.Flow

class ObjektfragebogenRepository(private val dao: ObjektfragebogenDao) {
    fun getByAufmassId(aufmassId: Long): Flow<ObjektfragebogenEntity?> = dao.getByAufmassId(aufmassId)

    suspend fun getById(id: Long): ObjektfragebogenEntity? = dao.getById(id)

    suspend fun insert(objektfragebogen: ObjektfragebogenEntity) = dao.insert(objektfragebogen)

    suspend fun update(objektfragebogen: ObjektfragebogenEntity) = dao.update(objektfragebogen)

    suspend fun delete(objektfragebogen: ObjektfragebogenEntity) = dao.delete(objektfragebogen)
}
