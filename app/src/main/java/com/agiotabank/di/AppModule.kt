package com.agiotabank.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.agiotabank.data.AppDatabase
import com.agiotabank.data.CardDao
import com.agiotabank.data.ContaDao
import com.agiotabank.data.ContaRepository
import com.agiotabank.data.EmprestimoDao
import com.agiotabank.data.EmprestimoRepository
import com.agiotabank.data.PixKeyDao
import com.agiotabank.data.PixKeyRepository
import com.agiotabank.data.InvestimentoDao
import com.agiotabank.data.InvestimentoRepository
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
        Room.databaseBuilder(ctx, AppDatabase::class.java, "agiota.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideContaDao(db: AppDatabase): ContaDao = db.contaDao()

    @Provides
    fun provideTransacaoDao(db: AppDatabase): TransacaoDao = db.transacaoDao()

    @Provides
    fun provideCardDao(db: AppDatabase): CardDao = db.cardDao()

    @Provides
    fun providePixKeyDao(db: AppDatabase): PixKeyDao = db.pixKeyDao()

    @Provides
    fun provideEmprestimoDao(db: AppDatabase): EmprestimoDao = db.emprestimoDao()

    @Provides
    fun provideInvestimentoDao(db: AppDatabase): InvestimentoDao = db.investimentoDao()

    @Provides
    @Singleton
    fun provideContaRepository(dao: ContaDao, dataStore: DataStore<Preferences>): ContaRepository =
        ContaRepository(dao, dataStore)

    @Provides
    @Singleton
    fun providePixKeyRepository(pixKeyDao: PixKeyDao): PixKeyRepository = PixKeyRepository(pixKeyDao)

    @Provides
    @Singleton
    fun provideTransacaoRepository(transacaoDao: TransacaoDao, contaDao: ContaDao): TransacaoRepository = TransacaoRepository(transacaoDao, contaDao)

    @Provides
    @Singleton
    fun provideEmprestimoRepository(emprestimoDao: EmprestimoDao, contaDao: ContaDao): EmprestimoRepository = EmprestimoRepository(emprestimoDao, contaDao)

    @Provides
    @Singleton
    fun provideInvestimentoRepository(investimentoDao: InvestimentoDao, contaDao: ContaDao): InvestimentoRepository =
        InvestimentoRepository(investimentoDao, contaDao)
}