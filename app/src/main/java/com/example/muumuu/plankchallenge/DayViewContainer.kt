package com.example.muumuu.plankchallenge

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView: TextView = view.findViewById(R.id.calendarDayText)
    val circle: ImageView = view.findViewById(R.id.circle)
}