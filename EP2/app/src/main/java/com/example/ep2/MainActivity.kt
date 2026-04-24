package com.example.ep2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.ep2.ui.fragments.CardStackFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID_DEFAULT = "default_channel"
        const val CHANNEL_ID_REMINDER = "reminder_channel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannels()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CardStackFragment())
                .commit()
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val defaultChannel = NotificationChannel(
                CHANNEL_ID_DEFAULT,
                "Основные уведомления",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Канал для основных уведомлений приложения"
            }

            val reminderChannel = NotificationChannel(
                CHANNEL_ID_REMINDER,
                "Напоминания",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Напоминания о повторении слов"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannels(listOf(defaultChannel, reminderChannel))
        }
    }

    fun showReminderNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID_REMINDER)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Время повторить слова!")
            .setContentText("Не забудьте повторить карточки сегодня")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1001, notification)
    }
}