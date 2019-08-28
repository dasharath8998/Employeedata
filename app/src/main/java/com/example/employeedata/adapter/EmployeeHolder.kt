package com.example.employeedata.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.custom_emp_list.view.*

class EmployeeHolder(v: View): RecyclerView.ViewHolder(v) {
    var img: ImageView? = null
    var name: TextView? = null
    var email: TextView? = null
    var bithdate: TextView? = null
    var gender: TextView? = null

    init {
        this.img = v.imgListProfile
        this.name = v.tvListName
        this.email = v.tvListEmail
        this.bithdate = v.tvListBirthday
        this.gender = v.tvListGender
    }

}