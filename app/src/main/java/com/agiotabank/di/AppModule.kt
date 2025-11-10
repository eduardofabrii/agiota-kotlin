package com.agiotabank.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.agiotabank.data.AppDatabase
import com.agiotabank.data.ContaDao
import com.agiotabank.data.ContaRepository
import com.agiotabank.data.TransacaoDao
import com.agiotabank.data.TransacaoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "agiota.db").build()

    @Provides
    fun provideContaDao(db: AppDatabase): ContaDao = db.contaDao()

    @Provides
    fun provideTransacaoDao(db: AppDatabase): TransacaoDao = db.transacaoDao()

    @Provides
    @Singleton
    fun provideContaRepository(dao: ContaDao, dataStore: DataStore<Preferences>): ContaRepository =
        ContaRepository(dao, dataStore)

    @Provides
    @Singleton
    fun provideTransacaoRepository(transacaoDao: TransacaoDao, contaDao: ContaDao): TransacaoRepository = TransacaoRepository(transacaoDao, contaDao)

}