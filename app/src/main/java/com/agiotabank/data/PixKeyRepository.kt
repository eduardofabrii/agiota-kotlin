package com.agiotabank.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class PixKeyRepository @Inject constructor(private val pixKeyDao: PixKeyDao) {
    suspend fun createPixKey(pixKey: PixKey) {
        pixKeyDao.insert(pixKey)
    }

    fun getPixKeys(contaId: Long): Flow<List<PixKey>> {
        return pixKeyDao.getKeysByContaId(contaId)
    }

    fun getPixKeyByChave(chave: String): Flow<PixKey?> {
        return pixKeyDao.getPixKeyByChave(chave)
    }
}
