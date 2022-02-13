package com.example.reminderapp.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "categories",
    indices = [
        Index("name",unique = true)
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long=0,
    @ColumnInfo(name = "name") val name: String
): Parcelable
