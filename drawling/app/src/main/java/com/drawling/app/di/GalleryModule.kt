package com.drawling.app.di

import com.drawling.app.gallery.data.remote.GalleryRepositoryImpl
import com.drawling.app.gallery.domain.repository.GalleryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GalleryModule {
    @Binds @Singleton
    abstract fun bindGalleryRepository(impl: GalleryRepositoryImpl): GalleryRepository
}
