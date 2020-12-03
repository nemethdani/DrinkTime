package hu.bme.aut.android.drinktime.model

import android.content.Context
import androidx.preference.PreferenceManager
import hu.bme.aut.android.drinktime.ui.settings.PersonalProfileSettingsFragment
import android.content.SharedPreferences
import hu.bme.aut.android.drinktime.MainActivity
import java.time.LocalDateTime

object Person {


    var name:String=""
    var age:Int=20
    var weight:Int=50
    var sex:Sex= Sex.female
    var sportIntensity= SportIntensity.sitting

    var lastHydrationTime=LocalDateTime.MIN
    var accumulatedHydration :Int =0


    fun setupPerson(context: Context?) {
        val preferences=PreferenceManager.getDefaultSharedPreferences(context)



        age= preferences.getString("age_preference",age.toString())?.toInt() ?:0
        name = preferences.getString("name_preference", name).toString()
        weight= preferences.getString("weight_preference", weight.toString())?.toInt()
                ?: 0
        val sex=preferences.getBoolean("sex_preference", false)
        if(sex){
            Person.sex=Sex.female
        }
        else{
            Person.sex=Sex.male
        }
        val intensity=preferences.getString("sport_list_preference", "Sitting work")
        sportIntensity=when(intensity){
            "Sitting work"-> SportIntensity.sitting
            "Light intensity sport" -> SportIntensity.light
            "Heavy intensity sport" -> SportIntensity.heavy
            else -> SportIntensity.sitting
        }
    }

    fun hydrate(hydration: Int) {
        accumulatedHydration+=hydration
        lastHydrationTime= LocalDateTime.now()
    }


    enum class Sex{male, female}
    enum class SportIntensity{sitting, light, heavy}

}