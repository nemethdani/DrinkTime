package hu.bme.aut.android.drinktime.scheduler

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import hu.bme.aut.android.drinktime.alarm.AlarmHelper
import hu.bme.aut.android.drinktime.alarm.DrinkAlarmBroadcastReceiver
import hu.bme.aut.android.drinktime.model.Person
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

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
        if(Person.hasToDrinkToday() && canDrinkToday()){
            return 0;
        }
        else{
            return SecsTillTomorrowFirstAlarm()
        }
    }

    private fun SecsTillTomorrowFirstAlarm(): Long {
        val now=LocalDateTime.now()
        val tomorrow=LocalDate.now().plusDays(1)
        val earliestAlarmTomorrow=
                if(isWeekend(tomorrow)) weekendEarliestAlarm
                else workdayEarliestAlarm
        val alarmTime=LocalDateTime.of(tomorrow, earliestAlarmTomorrow)
        return ChronoUnit.SECONDS.between(now, alarmTime)

    }

    private fun canDrinkToday(): Boolean {
        val now=LocalTime.now()
        val today=LocalDate.now()
        val weekend = isWeekend(today)
        if(weekend){
            return now.isBefore(weekendLatestAlarm)
        }
        else{
            return now.isBefore(workdayLatestAlarm)
        }
    }

    private fun isWeekend(day: LocalDate): Boolean {
        val weekend = (day.dayOfWeek == DayOfWeek.SATURDAY || day.dayOfWeek == DayOfWeek.SUNDAY)
        return weekend
    }
}