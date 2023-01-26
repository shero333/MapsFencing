package com.example.taskday1application.main.repository.utils

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseInjection {
    @Singleton
    @Provides
    fun provideDatabase(context: Application): DAO {
        val todoDatabase: TodoDatabase? = TodoDatabase.getInstance(context)
        return todoDatabase!!.Dao()
    }
}