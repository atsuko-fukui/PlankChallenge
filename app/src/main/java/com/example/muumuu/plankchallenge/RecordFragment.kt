package com.example.muumuu.plankchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.muumuu.plankchallenge.viewmodel.RecordViewModel
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.text.SimpleDateFormat
import java.util.*

class RecordFragment : Fragment() {

    private val calendarView: CalendarView
        get() = view!!.findViewById(R.id.calendarView)

    private val viewModel = RecordViewModel()
    private val disposable = CompositeDisposable()
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

                val isRecorded = recordList
                    .firstOrNull {
                        it == day.date
                    } != null
                container.circle.isVisible = isRecorded
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
        disposable.addAll(
            viewModel.fetchAllRecord(context)
                .subscribeOn(Schedulers.io())
                .subscribe(),

            viewModel.observeAllRecord()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    this.recordList = it.map {
                        LocalDate.parse(dateFormat.format(it.date))
                    }

                    calendarView.notifyCalendarChanged()
                }
        )
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
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
        val textView = view.findViewById<TextView>(R.id.monthText)
    }
}