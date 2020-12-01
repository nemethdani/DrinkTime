package hu.bme.aut.android.drinktime.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import hu.bme.aut.android.drinktime.R
import hu.bme.aut.android.drinktime.model.Person

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
        Person.setupPerson(this.context)

    }
}