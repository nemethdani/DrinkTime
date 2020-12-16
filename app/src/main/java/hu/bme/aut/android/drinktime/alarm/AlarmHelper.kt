package hu.bme.aut.android.drinktime.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context

class AlarmHelper {
    companion object {
        fun scheduleAlarm(ctx: Context, delaySeconds: Int, pendingIntent: PendingIntent) {
            val triggerDateTime = System.currentTimeMillis() + delaySeconds * 1000
            val alarmManager =
                ctx.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            alarmManager?.set(AlarmManager.RTC_WAKEUP, triggerDateTime, pendingIntent)
        }

        fun cancelAlarm(pendingIntent: PendingIntent, ctx:Context){
            val alarmManager =
                    ctx.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            alarmManager?.cancel(pendingIntent)
        }
    }
}