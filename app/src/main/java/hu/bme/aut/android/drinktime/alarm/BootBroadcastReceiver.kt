package hu.bme.aut.android.drinktime.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hu.bme.aut.android.drinktime.notification.NotificationHelper
import hu.bme.aut.android.drinktime.scheduler.Scheduler

class BootBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {

            NotificationHelper.createNotificationChannels(context)
            //Scheduler.reset()
            Scheduler.schedule(snoozeMode = false, context)
        }
    }
}