package com.example.lab8.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lab8.data.model.Task


interface TaskDao {
    // Obtiene todas las tareas ordenadas por ID descendente (las más nuevas primero)
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    suspend fun getAllTasks(): List<Task>

    // Inserta una nueva tarea
    @Insert
    suspend fun insertTask(task: Task)

    // Actualiza una tarea existente (usado para marcar como completada y editar)
    @Update
    suspend fun updateTask(task: Task)

    // Elimina todas las tareas
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}
