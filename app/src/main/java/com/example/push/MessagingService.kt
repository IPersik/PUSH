package com.example.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


private val PUSH_KEY_TITLE = "title"
private val PUSH_KEY_MESSAGE = "message"
private val CUSTOM_FIELD = "custom"
private val CHANNEL_ID = "channel_id"
private val NOTIFICATION_ID = 37
private val TAG = "Pushes"

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //здесь отправляем полученный токен на сервер (на наш - проекта)

        //если нужно получить токен еще раз
        /* FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
             //здесь мы снова получили наш токен, если нужно!
         }*/
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val custom = message.data[CUSTOM_FIELD]
        Log.i(TAG, custom ?: "no data!")

        if (message.data.isNotEmpty()) {
            handleDataMessage(message.data.toMap())
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        //это не нужно, чтобы показать уведомление пуша! Просто инфа о том, как создать уведомление самому.
        /*val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]

        if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
            showNotification(title, message)
        }*/
    }

    private fun showNotification(title: String, message: String) {
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentTitle(title)
                setContentText(message)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "Push-уведомления"
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }
}