package com.drawling.app.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.drawling.app.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DrawlingFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val data = message.data
        when (data["type"]) {
            "surprise" -> showSurpriseNotification(data["surpriseId"] ?: "")
            else -> showNotification(message.notification?.title ?: "Drawling", message.notification?.body ?: "", null)
        }
    }
    override fun onNewToken(token: String) { super.onNewToken(token) /* TODO: Send to backend */ }
    private fun showSurpriseNotification(surpriseId: String) {
        val intent = Intent(this, MainActivity::class.java).apply { putExtra("surpriseId", surpriseId); flags = Intent.FLAG_ACTIVITY_SINGLE_TOP }
        showNotification("🎁 Surprise drawing!", "Your partner sent you a drawing!", PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE))
    }
    private fun showNotification(title: String, body: String, pendingIntent: PendingIntent?) {
        val channelId = "drawling_channel"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) manager.createNotificationChannel(NotificationChannel(channelId, "Drawling", NotificationManager.IMPORTANCE_HIGH))
        val builder = NotificationCompat.Builder(this, channelId).setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(title).setContentText(body).setAutoCancel(true).apply { pendingIntent?.let { setContentIntent(it) } }
        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
