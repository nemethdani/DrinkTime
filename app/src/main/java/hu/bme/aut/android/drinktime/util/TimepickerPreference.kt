//https://gist.githubusercontent.com/hafizrahman/58529caac67cd8fefa17992de0877093/raw/11e640ae4fb4e30a122f86b6a8d49d5075f33f14/TimePickerPreference.kt

package hu.bme.aut.android.drinktime.util

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.TimePicker
import androidx.preference.DialogPreference
import androidx.preference.PreferenceDialogFragmentCompat

// This class is used in our preference where user can pick a time for notifications to appear.
// Specifically, this class is responsible for saving/retrieving preference data.
class TimepickerPreference(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {

    // Get saved preference value (in minutes from midnight, so 1 AM is represented as 1*60 here
    fun getPersistedMinutesFromMidnight(): Int {
        return super.getPersistedInt(DEFAULT_MINUTES_FROM_MIDNIGHT)
    }

    // Save preference
    fun persistMinutesFromMidnight(minutesFromMidnight: Int) {
        super.persistInt(minutesFromMidnight)
        notifyChanged()
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        super.onSetInitialValue(defaultValue)
        summary = minutesFromMidnightToHourlyTime(getPersistedMinutesFromMidnight())
    }

    // Mostly for default values
    companion object {
        // By default we want notification to appear at 9 AM each time.
        private const val DEFAULT_HOUR = 9
        const val DEFAULT_MINUTES_FROM_MIDNIGHT = DEFAULT_HOUR * 60
    }

}

class TimePickerPreferenceDialog : PreferenceDialogFragmentCompat() {

    lateinit var timepicker: TimePicker

    override fun onCreateDialogView(context: Context?): View {
        timepicker = TimePicker(context)
        return timepicker
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)

        val minutesAfterMidnight = (preference as TimepickerPreference)
            .getPersistedMinutesFromMidnight()
        timepicker.setIs24HourView(true)
        timepicker.hour = minutesAfterMidnight / 60
        timepicker.minute = minutesAfterMidnight % 60
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        // Save settings
        if(positiveResult) {
            val minutesAfterMidnight = (timepicker.hour * 60) + timepicker.minute
            (preference as TimepickerPreference).persistMinutesFromMidnight(minutesAfterMidnight)
            preference.summary = minutesFromMidnightToHourlyTime(minutesAfterMidnight)

        }
    }



    companion object {
        fun newInstance(key: String): TimePickerPreferenceDialog {
            val fragment = TimePickerPreferenceDialog()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle

            return fragment
        }
    }
}

fun minutesFromMidnightToHourlyTime(minutesAfterMidnight: Int): CharSequence? {
    return (minutesAfterMidnight/60).toString()+":"+(minutesAfterMidnight%60).toString()
}