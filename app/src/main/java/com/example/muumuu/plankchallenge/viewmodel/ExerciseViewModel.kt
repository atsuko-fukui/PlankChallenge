package com.example.muumuu.plankchallenge.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.muumuu.plankchallenge.ExerciseFragment
import com.example.muumuu.plankchallenge.model.AppDatabase
import com.example.muumuu.plankchallenge.model.Record
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExerciseViewModel : ViewModel() {

    val timer = MutableLiveData<Long>()

    fun initTimer(context: Context) {
        timer.postValue(getTimerDuration(context) * 100)
    }

    fun startTimer(duration: Long, context: Context) {
        viewModelScope.launch {
            for (i in 1..duration * 100) {
                delay(10)
                timer.postValue((duration * 100) - i)
            }

            val dao = AppDatabase.getInstance(context).recordDao()
            val existingRecordCount = dao.getAll().size
            dao.insert(Record(existingRecordCount + 1, System.currentTimeMillis()))
        }
    }

    private fun getTimerDuration(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getLong(
                ExerciseFragment.KEY_TIMER_DURATION,
                ExerciseFragment.EXERCISE_DURATION_IN_SECOND
            )

}