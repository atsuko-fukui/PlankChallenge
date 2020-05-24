package com.example.muumuu.plankchallenge.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muumuu.plankchallenge.model.Record
import com.example.muumuu.plankchallenge.model.RecordDao
import com.example.muumuu.plankchallenge.util.singleArgViewModelFactory
import kotlinx.coroutines.launch

class RecordViewModel(private val dao: RecordDao) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::RecordViewModel)
    }

    val recordList = MutableLiveData<List<Record>>(emptyList())

    fun fetchAllRecord() {
        viewModelScope.launch {
            recordList.postValue(dao.getAll())
        }
    }
}