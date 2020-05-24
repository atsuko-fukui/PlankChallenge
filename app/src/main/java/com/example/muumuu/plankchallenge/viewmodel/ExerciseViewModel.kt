package com.example.muumuu.plankchallenge.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muumuu.plankchallenge.ExerciseFragment
import com.example.muumuu.plankchallenge.model.Record
import com.example.muumuu.plankchallenge.model.RecordDao
import com.example.muumuu.plankchallenge.util.Event
import com.example.muumuu.plankchallenge.util.singleArgViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExerciseViewModel(private val dao: RecordDao) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::ExerciseViewModel)
    }

    val timer = MutableLiveData<Long>()
    val onCompleteSavingRecord = MutableLiveData<Event<Boolean>>()

    fun initTimer(sp: SharedPreferences) {
        timer.postValue(getTimerDuration(sp) * 100)
    }

    fun startTimer(duration: Long, currentTime: Long) {
        viewModelScope.launch {
            for (i in 1..duration * 100) {
                delay(10)
                timer.postValue((duration * 100) - i)
            }

            val existingRecordCount = dao.getAll().size
            dao.insert(Record(existingRecordCount + 1, currentTime))
            onCompleteSavingRecord.postValue(Event(true))
        }
    }

    private fun getTimerDuration(sp: SharedPreferences) =
        sp.getLong(
            ExerciseFragment.KEY_TIMER_DURATION,
            ExerciseFragment.EXERCISE_DURATION_IN_SECOND
        )
}