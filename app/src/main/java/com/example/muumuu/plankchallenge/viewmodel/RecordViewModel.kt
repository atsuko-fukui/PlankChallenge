package com.example.muumuu.plankchallenge.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muumuu.plankchallenge.model.AppDatabase
import com.example.muumuu.plankchallenge.model.Record
import kotlinx.coroutines.launch

class RecordViewModel: ViewModel() {

    val recordList = MutableLiveData<List<Record>>(emptyList())

    fun fetchAllRecord(context: Context) {
        viewModelScope.launch {
            val dao = AppDatabase.getInstance(context).recordDao()
            recordList.postValue(dao.getAll())
        }
    }
}