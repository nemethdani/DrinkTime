package hu.bme.aut.android.drinktime.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import hu.bme.aut.android.drinktime.DrinkTimeApplication.Companion.Person
import hu.bme.aut.android.drinktime.MainActivity
import hu.bme.aut.android.drinktime.R
import hu.bme.aut.android.drinktime.model.DrinkType

import kotlinx.android.synthetic.main.fragment_drink.*



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DrinkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrinkFragment(val drinkType: DrinkType, private val onDrinkListener: OnDrinkListener) : Fragment(),
    SeekBar.OnSeekBarChangeListener{



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvDrinkName.text=drinkType.name
        tvHydration.text=getString(R.string.hydration_1_d_drink_fragment, drinkType.hydration)
        ibtnDrink.setOnClickListener {
            drinkType.drink(Person, seekBar.milliLiter.toInt(), requireContext())
            onDrinkListener.onDrink()
        }
        seekBar.milliLiter=drinkType.defaultVolumeMl.toLong()
        tvVolumeToDrink.text=getString(R.string.volume_to_drink_1_d_ml_drink_fragment, seekBar.milliLiter)
        /*when(drinkType.id){
            0->ibtnDrink.setImageResource(R.drawable.water)
            1->ibtnDrink.setImageResource(R.drawable.fruit_juice)
            2->ibtnDrink.setImageResource(R.drawable.coffee)
        }*/


        seekBar.setOnSeekBarChangeListener(this)
    }

    fun setSeekBarMl(milliLiter: Int){
        seekBar.milliLiter= milliLiter.toLong()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        tvVolumeToDrink.text=getString(R.string.volume_to_drink_1_d_ml_drink_fragment, seekBar?.milliLiter)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }


}

interface OnDrinkListener {
    fun onDrink():Unit
}

var SeekBar.milliLiter: Long
    get() {return (progress*maxMilliLiter/max).toLong()}
    set(value:Long){progress= ((value*max)/maxMilliLiter).toInt()}

private val SeekBar.maxMilliLiter:Int
    get(){return 500}