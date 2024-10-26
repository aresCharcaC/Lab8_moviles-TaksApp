package com.example.lab8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.lab8.notification.TaskNotificationWorker
import com.example.lab8.ui.screens.TaskApp
import com.example.lab8.ui.theme.Lab8Theme
import com.example.lab8.ui.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Lab8Theme {
                TaskApp(viewModel)
            }
        }

        scheduleTaskNotification()
    }

    private fun scheduleTaskNotification() {
        val workRequest = PeriodicWorkRequestBuilder<TaskNotificationWorker>(15, TimeUnit.MINUTES)
            .build()

        // Usar ApplicationContext para evitar errores con 'this'
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}