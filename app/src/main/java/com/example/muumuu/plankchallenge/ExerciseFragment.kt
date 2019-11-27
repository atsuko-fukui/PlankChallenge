package com.example.muumuu.plankchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
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
        start.setOnClickListener {
            startTimer()
        }
    }

    private fun startTimer() {
        disposable.add(
            Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(EXERCISE_DURATION_IN_SECOND)
                .doOnNext {
                    timer.text = (EXERCISE_DURATION_IN_SECOND - it - 1).toInt().toString()
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