package com.example.lab8.domain

import com.example.lab8.data.model.Task
import com.example.lab8.data.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    fun getAllTasks(): Flow<List<Task>> = repository.getAllTasks()

    suspend fun addTask(description: String) {
        val task = Task(description = description)
        repository.insertTask(task)
    }

    suspend fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        repository.updateTask(updatedTask)
    }

    suspend fun deleteAllTasks() {
        repository.deleteAllTasks()
    }
}