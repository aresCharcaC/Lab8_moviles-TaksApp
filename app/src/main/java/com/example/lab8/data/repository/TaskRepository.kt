package com.example.lab8.data.repository

import com.example.lab8.data.model.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor() {
    private val database = FirebaseDatabase.getInstance()
    private val tasksRef = database.getReference("tasks")

    fun getTasks(): Flow<List<Task>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasks = snapshot.children.mapNotNull { dataSnapshot ->
                    try {
                        Task(
                            id = dataSnapshot.key ?: "",
                            description = dataSnapshot.child("description").getValue(String::class.java) ?: "",
                            isCompleted = dataSnapshot.child("isCompleted").getValue(Boolean::class.java) ?: false
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                trySend(tasks)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de errores
            }
        }

        tasksRef.addValueEventListener(listener)
        awaitClose { tasksRef.removeEventListener(listener) }
    }

    suspend fun addTask(description: String) {
        val newTaskRef = tasksRef.push()
        val taskData = hashMapOf(
            "description" to description,
            "isCompleted" to false
        )
        newTaskRef.setValue(taskData).await()
    }

    suspend fun toggleTaskCompletion(taskId: String, isCompleted: Boolean) {
        tasksRef.child(taskId).child("isCompleted").setValue(isCompleted).await()
    }

    suspend fun updateTaskDescription(taskId: String, newDescription: String) {
        tasksRef.child(taskId).child("description").setValue(newDescription).await()
    }

    suspend fun deleteAllTasks() {
        tasksRef.removeValue().await()
    }
}