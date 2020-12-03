package hu.bme.aut.android.drinktime.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import hu.bme.aut.android.drinktime.R

class DrinkView:FrameLayout{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    init{
        LayoutInflater.from(context).inflate(R.layout.drink_view,this, true)
    }
}


