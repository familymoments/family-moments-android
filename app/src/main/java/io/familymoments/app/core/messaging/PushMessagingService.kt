package io.familymoments.app.core.messaging

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.familymoments.app.R
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class PushMessagingService: FirebaseMessagingService() {

    private lateinit var source: UserInfoPreferencesDataSourceImpl

    override fun onCreate() {
        super.onCreate()
        Timber.i("PushMessagingService created")
        setSharedPreferences()
        createNotificationChannel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            source.saveFCMToken(token)
        }
    }

    companion object {
        private const val USER_INFO_PREFERENCES_KEY = "user_info"
    }

    private fun setSharedPreferences() {
        val preferences = getSharedPreferences(USER_INFO_PREFERENCES_KEY, MODE_PRIVATE)
        source = UserInfoPreferencesDataSourceImpl(preferences)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.d("PushMessagingService From: ${remoteMessage.from}")


        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: ${remoteMessage.data}")

            val channelId = getString(R.string.default_notification_channel_id)
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_my_page_notification)
                .setContentTitle("title")
                .setContentText("content")
                .setPriority(NotificationCompat.PRIORITY_MAX)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                if (ActivityCompat.checkSelfPermission(
                        this@PushMessagingService,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Timber.e("PushMessagingService", "No permission to post notifications")
                    return@with
                }
                // notificationId is a unique int for each notification that you must define.
                notify(198765432, builder.build())
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.d("Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "name" //getString(R.string.channel_name)
        val descriptionText = "description" // getString(R.string.channel_description)

        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(getString(R.string.default_notification_channel_id), name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
