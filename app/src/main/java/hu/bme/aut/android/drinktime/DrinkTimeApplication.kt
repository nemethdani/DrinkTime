package hu.bme.aut.android.drinktime

import android.app.Application
import hu.bme.aut.android.drinktime.model.Person
import hu.bme.aut.android.drinktime.scheduler.Scheduler

class DrinkTimeApplication : Application() {

    companion object {
        lateinit var Person: Person
            private set
        lateinit var Scheduler:Scheduler
    }

    override fun onCreate() {
        super.onCreate()
        Person=Person()
        Scheduler=Scheduler()
    }
}
