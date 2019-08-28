package com.example.employeedata.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.employeedata.R
import com.example.employeedata.activity.MainActivity
import com.example.employeedata.database.EmployeeDatabase
import com.example.employeedata.fragment.EmployeeAddFragment
import com.example.employeedata.fragment.EmployeeListFragment
import com.example.employeedata.model.Employee
import com.example.employeedata.utils.Utils
import kotlinx.android.synthetic.main.employee_list.*
import kotlin.math.acos

class EmployeeAdater(var context: Context, var list: MutableList<Employee>?): RecyclerView.Adapter<EmployeeHolder>() {

    var db = EmployeeDatabase.getInstance(context)
    override fun onBindViewHolder(holder: EmployeeHolder, position: Int) {

        //Binding data
        Glide.with(context).load(list?.get(position)?.img).placeholder(R.drawable.ic_account_circle_black_24dp).into(holder.img!!)
        holder.name?.text = "Name: ${list?.get(position)?.name}"
        holder.email?.text = "Email:${list?.get(position)?.email}"
        holder.bithdate?.text = "Birthdate: ${list?.get(position)?.birthdate}"
        holder.gender?.text = "Gender: ${list?.get(position)?.gender}"

        //Click event on particular item
        holder.itemView.setOnClickListener {
            var bundle = Bundle()
            var empAddFrag = EmployeeAddFragment()
            bundle.putLong(Utils.EMPLOYEE_ID, list?.get(position)?.id!!)
            empAddFrag.arguments = bundle

            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.fragMain,empAddFrag).addToBackStack(null).commit()
        }

        //LongClick event on particular item
        holder.itemView.setOnLongClickListener {
            var employee = list?.get(position)
            var alert = AlertDialog.Builder(context)
            alert.setMessage("Delete employee...")
            alert.setTitle("Alert")
            alert.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    removeEmployee(employee!!,position)
                }
            })
            alert.setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0?.cancel()
                }
            }).show()
            false
        }

    }

    /*
    * Returning inflated custom ui
    * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeHolder {
        return EmployeeHolder(LayoutInflater.from(context).inflate(R.layout.custom_emp_list,parent,false))
    }

    /*
    * return list size
    * */
    override fun getItemCount(): Int = list?.size!!

    /*
    * Removing item on longpress
    * */
    fun removeEmployee(employee: Employee,position: Int) {
        Thread(Runnable {
            kotlin.run {

                db.employeeDao().deleteEmployee(employee.id!!)
                list?.remove(employee)
                (context as MainActivity).runOnUiThread {
                    notifyDataSetChanged()
                }

                if(list?.size == 0){
                    (context as MainActivity).runOnUiThread {
                        (context as MainActivity).tvNDF.visibility = View.VISIBLE
                    }
                }
            }
        }).start()
    }

    fun filteredList(fillterdNames: MutableList<Employee>){
        this.list = fillterdNames
        notifyDataSetChanged()
    }
}
