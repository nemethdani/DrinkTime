package hu.bme.aut.android.drinktime.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import hu.bme.aut.android.drinktime.MainActivity
import hu.bme.aut.android.drinktime.R
import hu.bme.aut.android.drinktime.ui.home.HomeFragment
import kotlinx.coroutines.Dispatchers.Default
import kotlin.random.Random


class NotificationHelper {
    companion object{


        fun createNotificationChannels(ctx: Context){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                DrinkTimeNotificationChannel.values().forEach {
                    val name=it.channelName
                    val decriptionText=it.channelDescription
                    val importance= NotificationManager.IMPORTANCE_DEFAULT
                    val channel= NotificationChannel(it.id, name, importance).apply {
                        description=decriptionText
                    }
                    with(NotificationManagerCompat.from(ctx)){
                        createNotificationChannel(channel)
                    }
                }
            }
        }

        fun createDrinkTimeNotification(ctx: Context) {
            /*val intent = Intent(ctx, MainActivity::class.java).apply {
                action = ACTION_SHOW_AD
                putExtra(AD_ID, ad.id)
            }
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)*/

            val args = Bundle()
            args.putBoolean(HomeFragment.FROM_NOTIFICATION, true)

            val pendingIntent = NavDeepLinkBuilder(ctx)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.mobile_navigation)
                    .setDestination(R.id.nav_home)
                    .setArguments(args)
                    .createPendingIntent()

            /*val snoozeIntent = Intent(ctx, SnoozeBroadcastReceiver::class.java)
            val snoozePendingIntent: PendingIntent =
                PendingIntent.getBroadcast(ctx, 0, snoozeIntent, 0)*/



            val builder =
                NotificationCompat.Builder(ctx, DrinkTimeNotificationChannel.DRINK_TIME.id)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Time to drink")
                    .setContentText("Time to hydrate yourself")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)

                    .setAutoCancel(true)

            with(NotificationManagerCompat.from(ctx)) {
                cancelAll()
                notify(Random.Default.nextInt(10000, 100000), builder.build())
            }
        }

        /*fun createPendingIntentForPromotionNotification(ctx: Context): PendingIntent =
            PendingIntent.getBroadcast(ctx, 0, Intent(ctx, PromoBroadcastReceiver::class.java), 0)

        fun createPromoNotification(ctx: Context) {
            val intent = Intent(ctx, MainActivity::class.java)
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(ctx, 0, intent, 0)

            val builder =
                NotificationCompat.Builder(ctx, AdBrowserNotificationChannel.PROMO.id)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Ne hagyd ki!")
                    .setContentText("Prémium előfizetés most 50% kedvezménnyel")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

            with(NotificationManagerCompat.from(ctx)) {
                notify(Random.Default.nextInt(10000, 100000), builder.build())
            }
        }*/
    }

}