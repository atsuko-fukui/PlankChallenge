package com.example.muumuu.plankchallenge.viewmodel

import android.content.Context
import com.example.muumuu.plankchallenge.model.AppDatabase
import com.example.muumuu.plankchallenge.model.Record
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class RecordViewModel() {

    private val records = BehaviorProcessor.create<List<Record>>()

    fun fetchAllRecord(context: Context): Completable{
        val dao = AppDatabase.getInstance(context).recordDao()
        return Completable.fromAction {
            records.onNext(dao.getAll())
        }
    }

    fun saveRecord(context: Context): Completable {
        val dao = AppDatabase.getInstance(context).recordDao()
        return Completable.fromAction {
            val existingRecordCount = dao.getAll().size
            dao.insert(Record(existingRecordCount + 1, System.currentTimeMillis()))
        }
    }


    fun observeAllRecord(): Flowable<List<Record>> = records.hide()
}