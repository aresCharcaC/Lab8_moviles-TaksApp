package com.example.lab8.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class TaskNotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        showNotification("Recordatorio de Tareas", "Recuerda completar tus tareas pendientes.")
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "task_notifications"
        val notificationId = 1

        // Crear el canal de notificaciones (solo necesario en Android 8.0 y superiores)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Tareas", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Crear la notificación
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Mostrar la notificación
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
