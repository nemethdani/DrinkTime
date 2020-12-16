package hu.bme.aut.android.drinktime.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.drinktime.R
import hu.bme.aut.android.drinktime.adapter.DrinkFragmentAdapter
import hu.bme.aut.android.drinktime.model.Person
import hu.bme.aut.android.drinktime.notification.NotificationHelper
import hu.bme.aut.android.drinktime.scheduler.OnScheduledListener
import hu.bme.aut.android.drinktime.scheduler.Scheduler
import kotlinx.android.synthetic.main.fragment_drink.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), OnDrinkListener, OnScheduledListener{

    companion object{
        val FROM_NOTIFICATION="FROM_NOTIFICATION"
    }

    private var fromNotification=false
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter=DrinkFragmentAdapter(childFragmentManager, this)
        Scheduler.onScheduledListener=this

        NotificationHelper.createNotificationChannels(this.requireContext())

        var fromNotification= false
        if (arguments?.getBoolean(FROM_NOTIFICATION) == true) fromNotification=true



        if(fromNotification){
            viewPager.currentItem=0 //water
            val mlToDrink:Long=Scheduler.defaultHydrationPerCaseMl.toLong()
            viewPager.seekBar.milliLiter=mlToDrink
        }

        setDrinkData()



    }

    private fun setDrinkData() {
        tvPlannedDrink.text = getString(R.string.planned_drink_for_today_1_d_ml_fragment_home,
                Person.requiredHydration())
        tvActualDrink.text = getString(R.string.actual_drink_today_1_d_ml_fragment_home,
                Person.accumulatedHydration)
        tvRemainingDrink.text = getString(R.string.yet_to_drink_today_1_d_ml_fragment_home,
                Person.yetToDrinkTodayMl())
    }

    override fun onDrink() {
        setDrinkData()
    }

    override fun onScheduled(secsTillNextAlarm: Int) {
        Snackbar.make(home_constraint_layout, "Mins till next alarm: "+secsTillNextAlarm,
                                    Snackbar.LENGTH_LONG).show()
    }
}


