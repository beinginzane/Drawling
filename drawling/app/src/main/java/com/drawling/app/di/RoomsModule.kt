package com.drawling.app.di

import com.drawling.app.rooms.data.remote.RoomRepositoryImpl
import com.drawling.app.rooms.domain.repository.RoomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RoomsModule {
    @Binds @Singleton
    abstract fun bindRoomRepository(impl: RoomRepositoryImpl): RoomRepository
}
