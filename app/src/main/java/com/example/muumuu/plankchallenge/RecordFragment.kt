package com.example.muumuu.plankchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.muumuu.plankchallenge.viewmodel.RecordViewModel
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.text.SimpleDateFormat
import java.util.*

class RecordFragment : Fragment() {

    private val calendarView: CalendarView
        get() = view!!.findViewById(R.id.calendarView)

    private var recordList = emptyList<LocalDate>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val titleFormatter = DateTimeFormatter.ofPattern("yyyy MM")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()

                val isToday = day.date == LocalDate.now()
                container.circleToday.isVisible = isToday

                val isRecorded = recordList
                    .firstOrNull {
                        it == day.date
                    } != null
                container.circleRecorded.isVisible = isRecorded

                container.textView.text = day.date.dayOfMonth.toString()
                context?.let {
                    container.textView.setTextColor(
                        ContextCompat.getColor(
                            it,
                            if (day.owner == DayOwner.THIS_MONTH) {
                                android.R.color.black
                            } else {
                                R.color.grey
                            }
                        )
                    )
                }
            }
        }
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text = titleFormatter.format(month.yearMonth)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupCalendar()

        val context = context ?: return
        val viewModel = ViewModelProviders.of(this)[RecordViewModel::class.java]
        viewModel.fetchAllRecord(context)
            viewModel.recordList.observe(this) { list ->
                this.recordList = list.map {
                    LocalDate.parse(dateFormat.format(it.date))
                }
                calendarView.notifyCalendarChanged()
            }
    }

    private fun setupCalendar() {
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
    }

    class MonthViewContainer(view: View) : ViewContainer(view) {
        val textView: TextView = view.findViewById(R.id.monthText)
    }
}