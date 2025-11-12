package com.agiotabank.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

class ContaRepository(
    private val contaDao: ContaDao,
    private val dataStore: DataStore<Preferences>
) {
    private val KEY_CONTA_ID = longPreferencesKey("conta_id")

    val contaIdFlow: Flow<Long?> = dataStore.data
        .map { prefs -> prefs[KEY_CONTA_ID] }

    @OptIn(ExperimentalCoroutinesApi::class)
    val contaLogadaFlow: Flow<Conta?> = contaIdFlow
        .flatMapLatest { id ->
            id?.let { contaDao.getById(it) } ?: flowOf(null)
        }

    suspend fun setLoggedContaId(id: Long?) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            if (id == null) prefs.remove(KEY_CONTA_ID) else prefs[KEY_CONTA_ID] = id
        }
    }

    suspend fun updateSaldo(conta: Conta, valor: Double): Int = withContext(Dispatchers.IO) {
        contaDao.updateSaldo(conta.id, valor)
    }

    suspend fun updateConta(conta: Conta) = withContext(Dispatchers.IO) {
        contaDao.update(conta)
    }

    suspend fun criarConta(
        nome: String,
        email: String,
        senha: String,
        cpf: String,
        telefone: String,
        saldoInicial: Double,
        tipo: TipoConta
    ): Conta = withContext(Dispatchers.IO) {
        val temp = Conta(
            nome = nome,
            email = email,
            senha = senha,
            cpf = cpf,
            telefone = telefone,
            saldo = saldoInicial,
            tipo = tipo
        )
        val id = contaDao.insert(temp)
        temp.copy(id = id)
    }

    suspend fun login(email: String, senha: String): Conta? = withContext(Dispatchers.IO) {
        val contalogada = contaDao.login(email, senha)
        if (contalogada != null) {
            setLoggedContaId(contalogada.id)
        }
        return@withContext contalogada
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        setLoggedContaId(null)
    }

    fun getContaById(id: Long): Flow<Conta?> = contaDao.getById(id)

    suspend fun findByPixKey(chave: String): Conta? = withContext(Dispatchers.IO) {
        contaDao.findByPixKey(chave)
    }

    suspend fun findByAgenciaAndNumero(agencia: String ,numero: String): Conta? = withContext(Dispatchers.IO) {
        contaDao.findByAgenciaAndNumero(agencia, numero)
    }

    suspend fun remover(conta: Conta) = withContext(Dispatchers.IO) {
        contaDao.remover(conta)
    }

}
