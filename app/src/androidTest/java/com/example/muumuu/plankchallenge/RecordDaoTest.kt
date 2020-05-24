package com.example.muumuu.plankchallenge

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.muumuu.plankchallenge.model.AppDatabase
import com.example.muumuu.plankchallenge.model.Record
import com.example.muumuu.plankchallenge.model.RecordDao
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RecordDaoTest {

    private lateinit var recordDao: RecordDao
    private lateinit var db: AppDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        recordDao = db.recordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetAll() = runBlockingTest {
        val record1 = Record(0, 100L)
        recordDao.insert(record1)
        val record2 = Record(1, 200L)
        recordDao.insert(record2)

        val result = recordDao.getAll()
        Truth.assertThat(result).isEqualTo(listOf(record1, record2))
    }
}