package com.lemol.notireader

import android.app.Notification
import android.app.Notification.Action
import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast

class NotiListener : NotificationListenerService() {

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Started Notireader Service", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Notireader Service Not Running", Toast.LENGTH_SHORT).show()
    }

    // Executed when notification is received
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        // Contents of the notification in such form: "sender : message"
        val contents = sbn!!.notification.tickerText.toString()

        val extras = sbn.notification.extras
        // val sender = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)
        // val chatroomName = extras.getCharSequence(Notification.EXTRA_SUB_TEXT) // null if PM
        val commArgv = text?.split(" ")
        val comm = commArgv?.get(0)

        // Log.d("NOTI_LISTENER", sbn.packageName + Notification.WearableExtender(sbn.notification).actions.toString())
        // Log.d("NOTI_LISTENER", activeNotifications.toString())

        if (sbn.packageName.equals("com.kakao.talk")) {
            // Log.d("NOTI_LISTENER", "(KakaoTalk) $contents")

            // Code below may work on later versions of Android, but doesn't work on 6.0.1
            // This is because direct actions were introduced with Android version 7.0
            // Thus switching to Wearable Extender which comes with Wear OS by Google
            // Needs to be installed prior to usage
            // https://play.google.com/store/apps/details?id=com.google.android.wearable.app

            val noti = Notification.WearableExtender(sbn.notification).actions

            // Check for Android API level 24 (Android 7.0) or above
            // if (Build.VERSION.SDK_INT >= 24){
            //     var noti = sbn!!.notification.actions
            // }

            // This loop seeks for possible actions within the notification
            for (action in noti) {
                if (!action.remoteInputs.isNullOrEmpty() &&
                        (action.title.toString().contains("reply", true) ||
                                action.title.toString().contains("답장"))) {
                    when (comm) {
                        "/start" -> {
                            actionReply(action, "Auto Reply")
                        }
                    }
                }
            }
        }
        else {
            // If the notification is from other source
            // Log.d("NOTI_LISTENER", "(Other) $contents");
        }
    }

    // This method replies to the notification
    // action is the action from the notification, msg is the content of the message to be sent
    // The app cannot interact with chats on its own since the app does not use KakaoTalk API
    // Thus the app requires to have received a notification before sending any messages
    private fun actionReply(action: Action, msg: String) {
        val intent = Intent()
        val bundle = Bundle()
        for (input in action.remoteInputs) bundle.putCharSequence(input.resultKey, msg)
        RemoteInput.addResultsToIntent(action.remoteInputs, intent, bundle)
        action.actionIntent.send(this, 0, intent)
    }
}