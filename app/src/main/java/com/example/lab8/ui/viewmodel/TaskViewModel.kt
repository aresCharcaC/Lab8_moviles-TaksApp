package com.example.lab8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab8.data.model.Task
import com.example.lab8.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _editingTask = MutableStateFlow<Task?>(null)
    val editingTask: StateFlow<Task?> = _editingTask

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository.getTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun addTask(description: String) {
        if (description.isBlank()) return
        viewModelScope.launch {
            repository.addTask(description.trim())
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(task.id, !task.isCompleted)
        }
    }

    fun startEditingTask(task: Task) {
        _editingTask.value = task
    }

    fun updateTaskDescription(taskId: String, newDescription: String) {
        if (newDescription.isBlank()) return
        viewModelScope.launch {
            repository.updateTaskDescription(taskId, newDescription.trim())
            _editingTask.value = null
        }
    }

    fun cancelEditing() {
        _editingTask.value = null
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            repository.deleteAllTasks()
        }
    }
}