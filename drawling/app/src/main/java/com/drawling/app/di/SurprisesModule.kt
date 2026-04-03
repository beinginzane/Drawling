package com.drawling.app.di

import com.drawling.app.surprises.data.remote.SurpriseRepositoryImpl
import com.drawling.app.surprises.domain.repository.SurpriseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SurprisesModule {
    @Binds @Singleton
    abstract fun bindSurpriseRepository(impl: SurpriseRepositoryImpl): SurpriseRepository
}
