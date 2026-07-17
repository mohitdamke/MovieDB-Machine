package com.mohit.moviedbmachine.di.repository

import com.mohit.moviedbmachine.data.repository.MovieRepoImpl
import com.mohit.moviedbmachine.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepo(
        movieRepoImpl: MovieRepoImpl
    ): MovieRepository

}