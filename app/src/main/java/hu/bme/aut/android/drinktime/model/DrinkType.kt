package hu.bme.aut.android.drinktime.model

class DrinkType(
        val id:Int,
        var name: String,
        var hydration: Int, /*100 means 100% */
        var defaultVolumeMl: Int /* in milliliters*/
) {
    fun drink(person: Person, volumeMl: Int){
        person.hydrate(volumeMl*hydration)
    }

    companion object{
        val types:List<DrinkType> =listOf(
            DrinkType(0, "water", 100, 200),
                DrinkType(1, "fruit juice", 80, 200),
                DrinkType(0, "coffee", -30, 50)
        )
    }

}