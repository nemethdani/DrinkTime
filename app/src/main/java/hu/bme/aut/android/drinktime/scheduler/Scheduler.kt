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

    var defaultHydrationPerCaseMl:Int=200
    var snoozeTimeMins:Int =5
    var maxSnoozeNumber:Int=2
    var waitTillNextAlarmFromLastSnoozeMins:Int=30
    var workdayEarliestAlarm:LocalTime= LocalTime.of(7,0)
    var workdayLatestAlarm:LocalTime= LocalTime.of(18,0)
    var weekendEarliestAlarm=LocalTime.of(10,0)
    var weekendLatestAlarm=LocalTime.of(18,0)

    private var usedSnoozes: Int=0
    val snoozeModeName="snooze_mode"
    

    fun schedule(snoozeMode:Boolean, ctx:Context) {

        val nextModeIsSnooze=nextModeIsSnooze()
        if(nextModeIsSnooze) usedSnoozes++
        else usedSnoozes=0
        val delay:Long= DelayTillFirstAlarm(snoozeMode,nextModeIsSnooze) //Secs
        val intent=Intent(ctx, DrinkAlarmBroadcastReceiver::class.java)
        intent.putExtra(snoozeModeName, nextModeIsSnooze)
        val pendingIntent=
                PendingIntent
                    .getBroadcast(ctx, 0, intent, 0)
        AlarmHelper.scheduleAlarm(ctx, delay, pendingIntent)
    }

    private fun nextModeIsSnooze(): Boolean {
        return usedSnoozes< maxSnoozeNumber
    }

    //Secs
    private fun DelayTillFirstAlarm(currentModeIsSnooze:Boolean, nextModeIsSnooze: Boolean): Long {
        if(nextModeIsSnooze){
            return snoozeTimeMins*60.toLong()
        }
        else if(currentModeIsSnooze) return waitTillNextAlarmFromLastSnoozeMins*60.toLong()
        else{
            return if(Person.hasToDrinkToday() && canDrinkToday()){
                0;
            } else{
                SecsTillTomorrowFirstAlarm()
            }
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

        return now.isBefore(latestAlarmToday())
    }

    private fun latestAlarmToday():LocalTime{
        return if(isWeekend(LocalDate.now())){
            weekendLatestAlarm
        }
        else workdayLatestAlarm
    }

    private fun isWeekend(day: LocalDate): Boolean {
        val weekend = (day.dayOfWeek == DayOfWeek.SATURDAY || day.dayOfWeek == DayOfWeek.SUNDAY)
        return weekend
    }

    fun reset(ctx: Context) {
        usedSnoozes=0
        val snoozeIntent=Intent(ctx, DrinkAlarmBroadcastReceiver::class.java)
        snoozeIntent.putExtra(snoozeModeName, true)
        var pendingIntent=
                PendingIntent
                        .getBroadcast(ctx, 0, snoozeIntent, 0)
        AlarmHelper.cancelAlarm(pendingIntent, ctx)

        val normalIntent=Intent(ctx, DrinkAlarmBroadcastReceiver::class.java)
        normalIntent.putExtra(snoozeModeName, false)
        pendingIntent=
                PendingIntent
                        .getBroadcast(ctx, 0, normalIntent, 0)
        AlarmHelper.cancelAlarm(pendingIntent, ctx)
    }

    fun scheduledHydrationMl(): Long {
        val numberOfDrinksToday=Person.yetToDrinkTodayMl()/ defaultHydrationPerCaseMl
        val numOfPeriods=numberOfDrinksToday-1
        return remainingTimeForTodayMin()/numOfPeriods
    }

    private fun remainingTimeForTodayMin(): Long {
        val now=LocalTime.now()
        return ChronoUnit.MINUTES.between(now, latestAlarmToday())

    }
}