package com.example.employeedata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "empTbl")
data class Employee(

    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @ColumnInfo(name = "imagePath")
    var img: String,

    @ColumnInfo(name = "empName")
    var name: String,

    @ColumnInfo(name = "empEmail")
    var email: String,

    @ColumnInfo(name = "empBirthdate")
    var birthdate: String,

    @ColumnInfo(name = "empGender")
    var gender: String,

    @ColumnInfo(name = "timeStamp")
    var time: Long

)