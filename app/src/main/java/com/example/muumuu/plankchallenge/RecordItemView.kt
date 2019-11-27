package com.example.muumuu.plankchallenge

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import java.text.SimpleDateFormat
import java.util.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class RecordItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        View.inflate(context, R.layout.view_record_item, this)
    }

    private val recordDate: TextView
        get() = findViewById(R.id.record_date)

    @ModelProp
    fun setRecordDate(date: Long) {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        recordDate.text = dateFormat.format(Date(date))
    }
}