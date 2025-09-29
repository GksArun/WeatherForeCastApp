package com.gks.weatherforecastapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun formatDate(timeStamp:Int):String{
    val sdf= SimpleDateFormat("EEE, MMM d")
    val date=java.util.Date(timeStamp.toLong()*1000)
    return sdf.format(date)
}

fun formatDecimal(item:Double):String{
    return " %.0f".format(item)
}