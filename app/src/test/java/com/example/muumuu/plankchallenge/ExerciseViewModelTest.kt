package com.example.muumuu.plankchallenge

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.muumuu.plankchallenge.model.Record
import com.example.muumuu.plankchallenge.model.RecordDao
import com.example.muumuu.plankchallenge.util.TestCoroutineRule
import com.example.muumuu.plankchallenge.util.getValueForTest
import com.example.muumuu.plankchallenge.viewmodel.ExerciseViewModel
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class ExerciseViewModelTest {

    @get:Rule
    val coroutineScope = TestCoroutineRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun initTimer() {
        runBlockingTest {
            val subject = ExerciseViewModel(mock())
            val sp = mock<SharedPreferences> {
                on(it.getLong(
                    ExerciseFragment.KEY_TIMER_DURATION,
                    ExerciseFragment.EXERCISE_DURATION_IN_SECOND
                )) doReturn 1L
            }
            subject.initTimer(sp)

            Truth.assertThat(subject.timer.getValueForTest()).isEqualTo(100L)
        }
    }

    @Test
    fun startTimer() {
        coroutineScope.runBlockTest {
            val recordList = listOf(Record(0, 100L))
            val currentTime = 1000L
            val newRecord = Record(2, currentTime)
            val recordDaoMock = mock<RecordDao> {
                on(it.getAll()) doReturn recordList
            }
            val duration = 2L
            val subject = ExerciseViewModel(recordDaoMock)
            subject.startTimer(duration, currentTime)
            advanceTimeBy(20_000)

            Truth.assertThat(subject.timer.getValueForTest()).isEqualTo(0)
            verify(recordDaoMock).insert(newRecord)
            Truth.assertThat(
                subject.onCompleteSavingRecord.getValueForTest()?.peekContent()
            ).isEqualTo(true)
        }
    }
}