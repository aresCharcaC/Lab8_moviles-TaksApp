package com.example.lab8.di

import android.content.Context
import androidx.room.Room
import com.example.lab8.data.local.TaskDao
import com.example.lab8.data.local.TaskDatabase
import com.example.lab8.data.repository.TaskRepository
import com.example.lab8.domain.TaskUseCase
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
    fun provideTaskDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        TaskDatabase::class.java,
        "task_database"
    ).build()

    @Provides
    @Singleton
    fun provideTaskDao(database: TaskDatabase) = database.taskDao()

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao) = TaskRepository(taskDao)

    @Provides
    @Singleton
    fun provideTaskUseCase(repository: TaskRepository) = TaskUseCase(repository)
}