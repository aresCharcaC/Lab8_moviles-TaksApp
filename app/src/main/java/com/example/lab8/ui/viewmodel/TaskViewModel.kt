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
    // Estado para la lista de tareas
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    // Estado para la tarea que se está editando
    private val _editingTask = MutableStateFlow<Task?>(null)
    val editingTask: StateFlow<Task?> = _editingTask

    // Estado para mostrar el diálogo de confirmación de eliminación
    private val _taskToDelete = MutableStateFlow<Task?>(null)
    val taskToDelete: StateFlow<Task?> = _taskToDelete

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

    // Muestra el diálogo de confirmación de eliminación
    fun confirmDeleteTask(task: Task) {
        _taskToDelete.value = task
    }

    // Cancela la eliminación de la tarea
    fun cancelDeleteTask() {
        _taskToDelete.value = null
    }

    // Elimina la tarea seleccionada
    fun deleteTask() {
        viewModelScope.launch {
            _taskToDelete.value?.let { task ->
                repository.deleteTask(task.id)
                _taskToDelete.value = null
            }
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            repository.deleteAllTasks()
        }
    }
}