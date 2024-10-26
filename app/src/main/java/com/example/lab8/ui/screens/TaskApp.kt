package com.example.lab8.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab8.ui.components.EditTaskDialog
import com.example.lab8.ui.components.TaskItem
import com.example.lab8.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp(viewModel: TaskViewModel) {
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var newTaskDescription by remember { mutableStateOf("") }
    val tasks by viewModel.tasks.collectAsState()
    val editingTask by viewModel.editingTask.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Campo de entrada para nueva tarea
            OutlinedTextField(
                value = newTaskDescription,
                onValueChange = { newTaskDescription = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    if (newTaskDescription.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.addTask(newTaskDescription)
                            newTaskDescription = ""
                        }) {
                            Icon(Icons.Default.Add, "Agregar tarea")
                        }
                    }
                }
            )

            // Lista de tareas
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggleCompletion = { viewModel.toggleTaskCompletion(task) },
                        onEditClick = { viewModel.startEditingTask(task) }
                    )
                }
            }

            // Botón para eliminar todas las tareas
            if (tasks.isNotEmpty()) {
                Button(
                    onClick = { showConfirmDeleteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Eliminar todas las tareas")
                }
            }
        }

        // Diálogo de edición
        editingTask?.let { task ->
            EditTaskDialog(
                task = task,
                onDismiss = { viewModel.cancelEditing() },
                onConfirm = { newDescription ->
                    viewModel.updateTaskDescription(task.id, newDescription)
                }
            )
        }

        // Diálogo de confirmación para eliminar todas las tareas
        if (showConfirmDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDeleteDialog = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar todas las tareas?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteAllTasks()
                            showConfirmDeleteDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}



