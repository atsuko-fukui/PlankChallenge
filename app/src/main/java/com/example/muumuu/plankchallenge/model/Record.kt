package com.example.muumuu.plankchallenge.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Record(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "date") val date: Long
)