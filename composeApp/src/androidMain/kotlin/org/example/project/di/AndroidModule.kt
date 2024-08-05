package org.example.project.di

import android.app.Application
import androidx.room.Room
import org.example.project.data.local.GadgetsAIDatabase
import org.example.project.domain.DB_NAME
import org.example.project.domain.local.dao.AmountDao
import org.example.project.domain.repo.AmountRepository
import org.koin.dsl.module

fun provideDataBase(application: Application): GadgetsAIDatabase =
    Room.databaseBuilder(
        application,
        GadgetsAIDatabase::class.java,
        DB_NAME
    ).fallbackToDestructiveMigration().build()

fun provideDao(database: GadgetsAIDatabase): AmountDao = database.amountDao()

val dataBaseModule = module {
    single { provideDataBase(get()) }
    single { provideDao(get()) }
}

val repositoryModule = module {
    single { AmountRepository(get()) }
}