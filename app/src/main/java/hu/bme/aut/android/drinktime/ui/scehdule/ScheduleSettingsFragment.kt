package hu.bme.aut.android.drinktime.ui.scehdule

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import hu.bme.aut.android.drinktime.DrinkTimeApplication.Companion.Scheduler
import hu.bme.aut.android.drinktime.R

import hu.bme.aut.android.drinktime.util.TimePickerPreferenceDialog
import hu.bme.aut.android.drinktime.util.TimepickerPreference

class ScheduleSettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.schedule_preferences, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if(preference is TimepickerPreference) {
            val timepickerdialog = TimePickerPreferenceDialog.newInstance(preference.key)
            timepickerdialog.setTargetFragment(this, 0)
            timepickerdialog.show(parentFragmentManager, "androidx.preference.PreferenceFragment.DIALOG")
        }
        else {
            super.onDisplayPreferenceDialog(preference)
        }
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
        Scheduler.setup(this.context)
    }
}