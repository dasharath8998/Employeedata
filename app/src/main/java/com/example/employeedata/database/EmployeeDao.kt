package com.example.employeedata.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.employeedata.model.Employee

/*
* Database Query
* */

@Dao
interface EmployeeDao {
    @Query("SELECT * from empTbl")
    fun getAllEmployee(): MutableList<Employee>

    @Query("Select * from empTbl where id = :eid")
    fun getEmployee(eid: Long):Employee

    @Query("Delete from empTbl where id = :eid")
    fun deleteEmployee(eid: Long)

    @Query("Update empTbl set imagePath = :img, empName = :name, empEmail = :email, empBirthdate = :birthdate, empGender = :gender, timeStamp = :time where id = :eid")
    fun updateEmployee(eid: Long, img: String, name: String, email: String, birthdate: String, gender: String, time: Long):Int

    @Insert
    fun insetEmployee(employee: Employee): Long
}