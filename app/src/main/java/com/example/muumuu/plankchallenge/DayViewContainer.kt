package com.example.muumuu.plankchallenge

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView: TextView = view.findViewById(R.id.calendarDayText)
    val circleRecorded: ImageView = view.findViewById(R.id.circle_recorded)
    val circleToday: ImageView = view.findViewById(R.id.circle_today)
}