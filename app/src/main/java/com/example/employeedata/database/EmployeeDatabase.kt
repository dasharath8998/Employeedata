package com.example.employeedata.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.employeedata.model.Employee

@Database(entities = arrayOf(Employee::class), version = 1)
abstract class EmployeeDatabase: RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao

    /*
    * return database object
    * */

    companion object{

        var instance: EmployeeDatabase? = null

        fun getInstance(context: Context): EmployeeDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context,EmployeeDatabase::class.java,"empDB").build()
            }
            return instance as EmployeeDatabase
        }
    }
}