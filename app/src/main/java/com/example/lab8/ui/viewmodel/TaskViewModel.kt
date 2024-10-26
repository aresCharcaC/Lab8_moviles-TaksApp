package com.example.lab8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab8.data.local.TaskDao
import com.example.lab8.data.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val dao: TaskDao) : ViewModel() {
    // Estado para la lista de tareas
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    // Estado para controlar el diálogo de edición
    private val _editingTask = MutableStateFlow<Task?>(null)
    val editingTask: StateFlow<Task?> = _editingTask

    init {
        loadTasks()
    }

    // Carga todas las tareas de la base de datos
    private fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = dao.getAllTasks()
        }
    }

    // Agrega una nueva tarea
    fun addTask(description: String) {
        if (description.isBlank()) return
        viewModelScope.launch {
            dao.insertTask(Task(description = description.trim()))
            loadTasks()
        }
    }

    // Marca una tarea como completada o pendiente
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            dao.updateTask(task.copy(isCompleted = !task.isCompleted))
            loadTasks()
        }
    }

    // Inicia la edición de una tarea
    fun startEditingTask(task: Task) {
        _editingTask.value = task
    }

    // Actualiza la descripción de una tarea
    fun updateTaskDescription(taskId: Int, newDescription: String) {
        if (newDescription.isBlank()) return
        viewModelScope.launch {
            _tasks.value.find { it.id == taskId }?.let { task ->
                dao.updateTask(task.copy(description = newDescription.trim()))
                loadTasks()
            }
            // Cierra el diálogo de edición
            _editingTask.value = null
        }
    }

    // Cancela la edición actual
    fun cancelEditing() {
        _editingTask.value = null
    }

    // Elimina todas las tareas
    fun deleteAllTasks() {
        viewModelScope.launch {
            dao.deleteAllTasks()
            loadTasks()
        }
    }
}