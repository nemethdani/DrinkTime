package hu.bme.aut.android.drinktime.scheduler

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import hu.bme.aut.android.drinktime.alarm.AlarmHelper
import hu.bme.aut.android.drinktime.model.Person
import java.time.LocalTime

object Scheduler {

    var snoozeTimeMins:Int =5
    var maxSnoozeNumber:Int=2
    var waitTillNextAlarmFromLastSnoozeMins:Int=30
    var workdayEarliestAlarm:LocalTime= LocalTime.of(7,0)
    var workdayLatestAlarm:LocalTime= LocalTime.of(18,0)
    var weekendEarliestAlarm=LocalTime.of(10,0)
    var weekendLatestAlarm=LocalTime.of(18,0)

    private var usedSnoozes: Int=0
    val usedSnoozesName="used_snoozes"
    

    fun schedule(usedSnoozes:Int, ctx:Context) {
        this.usedSnoozes=usedSnoozes
        val delay:Long= DelayTillFirstAlarm() //Secs
        val intent=Intent(ctx, DrinkAlarmBroadcastReceiver::class.java)
        intent.putExtra(usedSnoozesName, usedSnoozes)
        val pendingIntent=
                PendingIntent
                    .getBroadcast(ctx, 0, intent, 0)
        AlarmHelper.scheduleAlarm(ctx, delay, pendingIntent)
    }

    //Secs
    private fun DelayTillFirstAlarm(): Long {
        if(Person.hasToDrinkToday && canDrinkToday){
            return 0;
        }
        else{
            return SecsTillTomorrowFirstAlarm()
        }
    }
}