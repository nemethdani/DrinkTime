package hu.bme.aut.android.drinktime.notification

enum class DrinkTimeNotificationChannel(
    val id: String,
    val channelName: String,
    val channelDescription: String
){
    DRINK_TIME("hu.bme.aut.android.drinktime.ui.home", "Time to Drink", "Notification about time to drink"),

}