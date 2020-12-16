package hu.bme.aut.android.drinktime.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hu.bme.aut.android.drinktime.DrinkTimeApplication.Companion.Scheduler
import hu.bme.aut.android.drinktime.notification.NotificationHelper


class BootBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {

            NotificationHelper.createNotificationChannels(context)
            //Scheduler.reset()
            Scheduler.schedule(snoozeMode = false, context)
        }
    }
}