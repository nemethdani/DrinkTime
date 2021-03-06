package hu.bme.aut.android.drinktime.scheduler

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import hu.bme.aut.android.drinktime.DrinkTimeApplication.Companion.Person
import hu.bme.aut.android.drinktime.alarm.AlarmHelper
import hu.bme.aut.android.drinktime.alarm.DrinkAlarmBroadcastReceiver

import java.lang.Long.min
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class Scheduler {

    var defaultHydrationPerCaseMl: Int = 200
    var snoozeTimeMins: Int = 5
    var maxSnoozeNumber: Int = 2
    var waitTillNextAlarmFromLastSnoozeMins: Int = 30
    var workdayEarliestAlarm: LocalTime = LocalTime.of(7, 0)
    var workdayLatestAlarm: LocalTime = LocalTime.of(18, 0)
    var weekendEarliestAlarm = LocalTime.of(10, 0)
    var weekendLatestAlarm = LocalTime.of(18, 0)

    private var usedSnoozes: Int = 0
    val snoozeModeName = "snooze_mode"

    lateinit var onScheduledListener: OnScheduledListener

    fun setup(context: Context?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        defaultHydrationPerCaseMl = preferences.getString(
            "defaultWaterMl",
            defaultHydrationPerCaseMl.toString()
        )?.toInt() ?: 200
        snoozeTimeMins = preferences.getString(
            "snoozeTimeMin",
            snoozeTimeMins.toString()
        )?.toInt() ?: 5
        maxSnoozeNumber = preferences.getString(
            "MaxNumOfSnoozes",
            maxSnoozeNumber.toString()
        )?.toInt() ?: 2
        waitTillNextAlarmFromLastSnoozeMins = preferences.getString(
            "waitTimeMin",
            waitTillNextAlarmFromLastSnoozeMins.toString()
        )?.toInt() ?: 30
        var mins: Int = 0
        mins = preferences.getInt("workday_earliest_time", 7 * 60)
        workdayEarliestAlarm = LocalTime.of(mins/60, mins%60)
        mins = preferences.getInt("workday_latest_time", 19 * 60)
        workdayLatestAlarm = LocalTime.of(mins/60, mins%60)
        mins = preferences.getInt("weekend_earliest_time", 10 * 60)
        weekendEarliestAlarm = LocalTime.of(mins/60, mins%60)
        mins = preferences.getInt("weekend_latest_time", 19 * 60)
        weekendLatestAlarm = LocalTime.of(mins/60, mins%60)

    }


    fun schedule(snoozeMode: Boolean, ctx: Context) {
        var delayMins: Int = 0
        if (snoozeMode) {
            if (usedSnoozes < maxSnoozeNumber) {
                delayMins = snoozeTimeMins
                usedSnoozes++
            } else {
                delayMins = min(
                    waitTillNextAlarmFromLastSnoozeMins.toLong(),
                    remainingTimeForTodayMin()
                ).toInt()
            }
        } else {
            if (canDrinkToday() && Person.hasToDrinkToday()) {
                delayMins = scheduledHydrationPeriodMins()
            } else {
                delayMins = (SecsTillTomorrowFirstAlarm() / 60).toInt()
            }
        }
        sendDrinkalarm(delayMins * 60, ctx)
    }


    fun sendDrinkalarm(delaySecs: Int, ctx: Context) {
        val intent = Intent(ctx, DrinkAlarmBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent
                .getBroadcast(ctx, 0, intent, 0)
        AlarmHelper.scheduleAlarm(ctx, delaySecs, pendingIntent)
        onScheduledListener.onScheduled(delaySecs)
    }

    private fun SecsTillTomorrowFirstAlarm(): Long {
        val now = LocalDateTime.now()
        val tomorrow = LocalDate.now().plusDays(1)
        val earliestAlarmTomorrow =
            if (isWeekend(tomorrow)) weekendEarliestAlarm
            else workdayEarliestAlarm
        val alarmTime = LocalDateTime.of(tomorrow, earliestAlarmTomorrow)
        return ChronoUnit.SECONDS.between(now, alarmTime)

    }

    private fun canDrinkToday(): Boolean {
        val now = LocalTime.now()

        return now.isBefore(latestAlarmToday())
    }

    private fun latestAlarmToday(): LocalTime {
        return if (isWeekend(LocalDate.now())) {
            weekendLatestAlarm
        } else workdayLatestAlarm
    }

    private fun isWeekend(day: LocalDate): Boolean {
        val weekend = (day.dayOfWeek == DayOfWeek.SATURDAY || day.dayOfWeek == DayOfWeek.SUNDAY)
        return weekend
    }

    fun reset(ctx: Context) {
        usedSnoozes = 0
        /*val snoozeIntent=Intent(ctx, DrinkAlarmBroadcastReceiver::class.java)
        snoozeIntent.putExtra(snoozeModeName, true)
        var pendingIntent=
                PendingIntent
                        .getBroadcast(ctx, 0, snoozeIntent, 0)
        AlarmHelper.cancelAlarm(pendingIntent, ctx)*/

        val normalIntent = Intent(ctx, DrinkAlarmBroadcastReceiver::class.java)
        //normalIntent.putExtra(snoozeModeName, false)
        var pendingIntent =
            PendingIntent
                .getBroadcast(ctx, 0, normalIntent, 0)
        AlarmHelper.cancelAlarm(pendingIntent, ctx)


    }

    fun scheduledHydrationPeriodMins(): Int {
        val numberOfDrinksToday = Person.yetToDrinkTodayMl() / defaultHydrationPerCaseMl
        val numOfPeriods = numberOfDrinksToday
        return (remainingTimeForTodayMin() / numOfPeriods).toInt()
    }

    private fun remainingTimeForTodayMin(): Long {
        val now = LocalTime.now()
        return ChronoUnit.MINUTES.between(now, latestAlarmToday())

    }

}

interface OnScheduledListener {
    fun onScheduled(secsTillNextAlarm: Int):Unit
}
