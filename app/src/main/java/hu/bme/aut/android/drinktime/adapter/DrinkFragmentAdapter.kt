package hu.bme.aut.android.drinktime.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.aut.android.drinktime.model.DrinkType
import hu.bme.aut.android.drinktime.ui.home.DrinkFragment

class DrinkFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return DrinkType.types.size
    }

    override fun getItem(position: Int): Fragment {
        return DrinkFragment(DrinkType.types[position])
    }
}