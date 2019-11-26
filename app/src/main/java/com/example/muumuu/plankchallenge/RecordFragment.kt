package com.example.muumuu.plankchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class RecordFragment : Fragment() {

    companion object {
        private const val EXERCISE_DURATION_IN_SECOND = 30L
    }
    private val timer: TextView
        get() = view!!.findViewById(R.id.timer)

    private val start: Button
        get() = view!!.findViewById(R.id.start)

    private val disposalbe = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start.setOnClickListener {
            startTimer()
        }
    }

    private fun startTimer() {
        disposalbe.add(
            Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(EXERCISE_DURATION_IN_SECOND)
                .doOnNext {
                    timer.text = (EXERCISE_DURATION_IN_SECOND - it).toInt().toString()
                }
                .doOnComplete {
                    saveRecord()
                }
                .subscribe()
        )
    }

    private fun saveRecord() {

    }

    override fun onPause() {
        super.onPause()
        disposalbe.clear()
    }
}