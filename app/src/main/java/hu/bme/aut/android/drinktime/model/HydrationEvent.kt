package hu.bme.aut.android.drinktime.model

import java.time.LocalDateTime

class HydrationEvent(val drink: DrinkType, val volumeMl:Int) {
    var localDateTime=LocalDateTime.now()

}

