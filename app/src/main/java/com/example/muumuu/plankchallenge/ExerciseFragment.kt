package com.example.muumuu.plankchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.muumuu.plankchallenge.viewmodel.RecordViewModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ExerciseFragment : Fragment() {

    companion object {
        private const val EXERCISE_DURATION_IN_SECOND = 30L
        const val KEY_TIMER_DURATION = "key_timer_duration"
        const val TAG_EDIT_TIMER_DIALOG = "tag_edit_timer_dialog"
    }

    private val timer: TextView
        get() = view!!.findViewById(R.id.timer)

    private val start: Button
        get() = view!!.findViewById(R.id.start)

    private val viewModel = RecordViewModel()

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timerDuration = getTimerDuration()
        start.setOnClickListener {
            startTimer(timerDuration)
        }
        timer.text = timerDuration.toString()
        timer.setOnClickListener {
            showTimerEditDialog(timerDuration)
        }
    }

    private fun getTimerDuration() =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getLong(KEY_TIMER_DURATION, EXERCISE_DURATION_IN_SECOND)

    private fun showTimerEditDialog(duration: Long) {
        activity?.supportFragmentManager?.let {
            EditTimerDialogFragment
                .createInstance(duration)
                .apply {
                    setListner {
                        timer.text = it.toString()
                    }
                }
                .show(it, TAG_EDIT_TIMER_DIALOG)
        }
    }

    private fun startTimer(duration: Long) {
        disposable.add(
            Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(duration)
                .doOnNext {
                    timer.text = (duration - it - 1).toInt().toString()
                }
                .subscribeOn(Schedulers.io())
                .doOnComplete {
                    saveRecord()
                }
                .subscribe()
        )
    }

    private fun saveRecord() {
        val context = context ?: return
        disposable.add(
            viewModel.saveRecord(context)
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }
}