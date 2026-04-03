package com.drawling.app.di

import com.drawling.app.auth.data.remote.AuthRepositoryImpl
import com.drawling.app.auth.domain.repository.AuthRepository
import com.drawling.app.canvas.data.remote.CanvasRepositoryImpl
import com.drawling.app.canvas.domain.repository.CanvasRepository
import com.drawling.app.gallery.data.remote.GalleryRepositoryImpl
import com.drawling.app.gallery.domain.repository.GalleryRepository
import com.drawling.app.rooms.data.remote.RoomRepositoryImpl
import com.drawling.app.rooms.domain.repository.RoomRepository
import com.drawling.app.surprises.data.remote.SurpriseRepositoryImpl
import com.drawling.app.surprises.domain.repository.SurpriseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindRoomRepository(impl: RoomRepositoryImpl): RoomRepository

    @Binds @Singleton
    abstract fun bindCanvasRepository(impl: CanvasRepositoryImpl): CanvasRepository

    @Binds @Singleton
    abstract fun bindGalleryRepository(impl: GalleryRepositoryImpl): GalleryRepository

    @Binds @Singleton
    abstract fun bindSurpriseRepository(impl: SurpriseRepositoryImpl): SurpriseRepository
}
