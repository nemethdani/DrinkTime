package hu.bme.aut.android.drinktime.ui.settings

import android.R.attr
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import hu.bme.aut.android.drinktime.DrinkTimeApplication.Companion.Person
import hu.bme.aut.android.drinktime.R


class PersonalProfileSettingsFragment : PreferenceFragmentCompat(),
                                        SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.personal_profile_preferences, rootKey)

    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this.context).
        registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        PreferenceManager.getDefaultSharedPreferences(this.context).
        unregisterOnSharedPreferenceChangeListener(this)
    }



    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {


        val cs :CharSequence =key ?:""
        val pref : Preference? = findPreference(cs)

        //Update the summary with user input data
        if (pref is EditTextPreference){
            //Get the user input data
            val prefixStr = sharedPreferences!!.getString(key, "")

            pref?.setSummary(prefixStr)
        }


        Person.setupPerson(this.context)

    }
}