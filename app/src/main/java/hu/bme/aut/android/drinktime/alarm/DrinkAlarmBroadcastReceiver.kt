package hu.bme.aut.android.drinktime.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hu.bme.aut.android.drinktime.scheduler.Scheduler

class DrinkAlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val snoozeMode=intent?.getBooleanExtra(Scheduler.snoozeModeName, true) ?: true
        Scheduler.schedule(snoozeMode, context)
    }

}
