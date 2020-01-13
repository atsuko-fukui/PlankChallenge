package com.example.muumuu.plankchallenge.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Query("SELECT * from record")
    suspend fun getAll(): List<Record>

    @Insert
    suspend fun insert(record: Record)
}