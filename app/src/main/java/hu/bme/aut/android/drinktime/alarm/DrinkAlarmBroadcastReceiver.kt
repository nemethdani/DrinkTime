package hu.bme.aut.android.drinktime.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hu.bme.aut.android.drinktime.DrinkTimeApplication.Companion.Scheduler
import hu.bme.aut.android.drinktime.notification.NotificationHelper


class DrinkAlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        //val snoozeMode=intent?.getBooleanExtra(Scheduler.snoozeModeName, true) ?: true
        Scheduler.schedule(snoozeMode = true, context) //repeat if not reagated
        NotificationHelper.createDrinkTimeNotification(context)

    }

}
