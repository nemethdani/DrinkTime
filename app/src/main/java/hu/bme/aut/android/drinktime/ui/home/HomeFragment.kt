package hu.bme.aut.android.drinktime.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.drinktime.R
import hu.bme.aut.android.drinktime.adapter.DrinkFragmentAdapter
import hu.bme.aut.android.drinktime.notification.NotificationHelper
import hu.bme.aut.android.drinktime.scheduler.Scheduler
import kotlinx.android.synthetic.main.fragment_drink.*
import kotlinx.android.synthetic.main.fragment_drink.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

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
        viewPager.adapter=DrinkFragmentAdapter(childFragmentManager)
        NotificationHelper.createNotificationChannels(this.requireContext())

        fromNotification= arguments?.get(FROM_NOTIFICATION) as Boolean

        if(fromNotification){
            viewPager.currentItem=0 //water
            val mlToDrink:Int=Scheduler.scheduledHydrationMl()
            viewPager.seekBar.milliLiter(mlToDrink)
        }



    }
}