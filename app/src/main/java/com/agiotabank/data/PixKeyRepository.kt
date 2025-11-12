package com.agiotabank.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PixKeyRepository @Inject constructor(private val pixKeyDao: PixKeyDao) {
    suspend fun createPixKey(pixKey: PixKey) {
        pixKeyDao.insert(pixKey)
    }

    fun getPixKeys(contaId: Long): Flow<List<PixKey>> {
        return pixKeyDao.getKeysByContaId(contaId)
    }
}
