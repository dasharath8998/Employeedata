package com.example.employeedata.utils

import android.app.DatePickerDialog
import android.content.Context
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import com.example.employeedata.listeners.DateSetListener
import java.util.*

object Utils {
    var REQUEST_CODE_READ_DATA = 123
    var START_ACTIVITY_FOR_IMGE = 11
    var EMPLOYEE_ID = "EmployeeId"

    fun openCalender(context: Context, cYear: Int, cMonth: Int, cDay: Int, dateSelectListener: DateSetListener){
        var dpd = DatePickerDialog(context,DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateSelectListener.onDateSelected(year,monthOfYear,dayOfMonth)
        },cYear,cMonth,cDay)

        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }

    fun getAge(year:Int,monthOfYear:Int,dayOfMonth:Int):String{

        var c = Calendar.getInstance()
        var cYear = c.get(Calendar.YEAR)
        var cMonth = c.get(Calendar.MONTH)
        var cDay = c.get(Calendar.DAY_OF_MONTH)
        var age:String=""
        if(cMonth < monthOfYear) {

            age = "${cYear - year - 1} Years, "
            if(dayOfMonth < cDay) {
                age+= (12 - (monthOfYear - cMonth)).toString() + " Months, "
                age+= "${cDay - dayOfMonth} Days"
            }else{
                age+= (11 - (monthOfYear - cMonth)).toString() + " Months, "
                age+= "${31 - (dayOfMonth - cDay)} Days"
            }

        }else if(cMonth > monthOfYear) {

            age+= (cYear - year).toString() + " Years, "
            if(dayOfMonth < cDay) {
                age+= (cMonth - monthOfYear).toString() + " Months, "
                age+= "${(cDay - dayOfMonth)} Days"
            }else{
                age+= (cMonth - monthOfYear - 1).toString() + " Months, "
                age+= "${31 - (dayOfMonth - cDay)} Days"
            }

        }else{
            age+= (cYear - year).toString() + " Years, "

            if(dayOfMonth < cDay) {
                age+= "0 Months, "
                age+= "${(cDay - dayOfMonth)} Days"
            }else{
                age+="11 Months, "
                age+= "${31 - (dayOfMonth - cDay)} Days"
            }
        }
        return age
    }

    fun stringToDate(date: String, format: String): String{
        var simpleDate = SimpleDateFormat(format)
        var parseDate = simpleDate.parse(date)
        var day = Integer.parseInt(DateFormat.format("dd",parseDate).toString())
        var month = Integer.parseInt(DateFormat.format("mm",parseDate).toString())
        var year = Integer.parseInt(DateFormat.format("yyyy",parseDate).toString())
        return getAge(year,month-1,day)
    }
}