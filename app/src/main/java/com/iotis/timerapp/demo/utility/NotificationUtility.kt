package com.iotis.timerapp.demo.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.iotis.timerapp.demo.R
import com.iotis.timerapp.demo.presentation.MainActivity

@ExperimentalMaterial3Api
@ExperimentalLifecycleComposeApi
object NotificationUtility {
    private const val CONST_CHANNEL_ID = "com.iotis.timer.demo"
    private const val CONST_CHANNEL_DESCRIPTION = "This is a iotis count down timer channel"
    private const val CONST_NOTIFICATION_TITLE = "Time's up..."
    private const val CONST_NOTIFICATION_TEXT = "Hey! Your time is over. Start again..."

    internal fun notifyUser(context: Context) {
        createNotificationChannel(context)

        val notificationId = (0..999).random()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CONST_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(CONST_NOTIFICATION_TITLE)
            .setContentText(CONST_NOTIFICATION_TEXT)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        /**
         * Create the NotificationChannel, but only on API 26+ because
         * the NotificationChannel class is new and not in the support library
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CONST_CHANNEL_ID
            val descriptionText = CONST_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CONST_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            /**
             * Register the channel with the system
             * */
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}