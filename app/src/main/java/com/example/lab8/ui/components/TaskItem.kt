package com.example.lab8.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.lab8.data.model.Task

@Composable
fun TaskItem(
    task: Task,
    onToggleCompletion: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Texto de la tarea
            Text(
                text = task.description,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (task.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Botones de acción
            Row {
                // Botón de editar
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, "Editar tarea")
                }

                // Botón de eliminar
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar tarea",
                        tint = Color.Red
                    )
                }

                // Botón de completar
                IconButton(onClick = onToggleCompletion) {
                    Icon(
                        if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = if (task.isCompleted) "Marcar como pendiente" else "Marcar como completada",
                        tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
        }
    }
}

// Diálogo de confirmación de eliminación
@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar eliminación") },
        text = { Text("¿Estás seguro de que deseas eliminar esta tarea?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EditTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var editedDescription by remember { mutableStateOf(task.description) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Editar tarea",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = { onConfirm(editedDescription) },
                        enabled = editedDescription.isNotBlank()
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}