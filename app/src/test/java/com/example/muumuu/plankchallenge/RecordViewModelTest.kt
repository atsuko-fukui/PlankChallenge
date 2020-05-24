package com.example.muumuu.plankchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.muumuu.plankchallenge.model.Record
import com.example.muumuu.plankchallenge.model.RecordDao
import com.example.muumuu.plankchallenge.util.TestCoroutineRule
import com.example.muumuu.plankchallenge.util.getValueForTest
import com.example.muumuu.plankchallenge.viewmodel.RecordViewModel
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class RecordViewModelTest {

    @get:Rule
    val coroutineScope = TestCoroutineRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun fetchAllRecord() {
        runBlockingTest {
            val recordList = listOf(Record(0, 100L))
            val recordDaoMock = mock<RecordDao> {
                on(it.getAll()) doReturn recordList
            }
            val subject = RecordViewModel(recordDaoMock)
            subject.fetchAllRecord()

            Truth.assertThat(subject.recordList.getValueForTest()).isEqualTo(recordList)
        }
    }
}