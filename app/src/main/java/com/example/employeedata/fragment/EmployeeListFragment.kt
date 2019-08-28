package com.example.employeedata.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.employeedata.R
import com.example.employeedata.activity.MainActivity
import com.example.employeedata.adapter.EmployeeAdater
import com.example.employeedata.database.EmployeeDatabase
import com.example.employeedata.model.Employee
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.employee_list.*
import kotlin.collections.ArrayList

class EmployeeListFragment: Fragment() {

    var list: MutableList<Employee>? = null
    var db: EmployeeDatabase? = null
    var eAdapter: EmployeeAdater? = null
    var filterdNames = ArrayList<Employee>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /*
    * onCreateView method
    * db variable initialization
    * */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        db = EmployeeDatabase.getInstance(context!!)
        activity?.drawer_layout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        activity?.toolbar?.setNavigationIcon(R.drawable.ic_dehaze_black_24dp)
        activity?.toolbar?.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(Gravity.LEFT)
        }

        MainActivity().hideKeyboard(activity!!)

        return inflater.inflate(R.layout.employee_list,container,false)
    }


    /*
    * setting list on employee adapter
    * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empRView.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)

        Thread(Runnable {
            kotlin.run {
                list= db?.employeeDao()?.getAllEmployee()
                if(list?.size == 0) {
                    tvNDF.visibility = View.VISIBLE
                }else{
                    activity?.runOnUiThread{
                        tvNDF.visibility = View.GONE
                        eAdapter = EmployeeAdater(context!!,list)
                        empRView.adapter = eAdapter
                        sortListByLastAdded()
                    }
                }
            }
        }).start()

        Log.i("onViewCreated1","called")

        etSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(editable: Editable?) {
                if(editable.toString() == ""){
                    filterdNames.clear()
                    displayData()
                }else {
                    filter(editable.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }


    /*
    * Display data by notifying
    * */
    fun displayData(){

        Thread(Runnable {
            kotlin.run {
                list = db?.employeeDao()?.getAllEmployee()
                if(list?.size == 0) {
                    tvNDF.visibility = View.VISIBLE
                }else{
                    activity?.runOnUiThread{
                        tvNDF.visibility = View.GONE
                        sortListByLastAdded()
                        eAdapter?.filteredList(list!!)
                    }
                }
            }
        }).start()
    }

    /*
    * filtering name
    * */
    fun filter(text:String) {

        filterdNames.clear()

        for (s in list!!)
        {
            if (s.name.toLowerCase().contains(text.toLowerCase()) || s.email.toLowerCase().contains(text.toLowerCase()))
            {
                filterdNames.add(s)
            }
        }
        eAdapter?.filteredList(filterdNames)
    }

    /*
    * sorting through name
    * */
    fun sortListByName(){
        list?.sortBy {
            it.name.toLowerCase()
        }
        eAdapter?.notifyDataSetChanged()
    }

    /*
    * sorting through email
    * */
    fun sortListByEmail(){
        list?.sortBy {
            it.email.toLowerCase()
        }
        eAdapter?.notifyDataSetChanged()
    }

    /*
    * sorting through employee added on last
    * */
    fun sortListByLastAdded(){
        list?.sortByDescending {
            it.time
        }
        eAdapter?.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.sortingName -> sortListByName()
            R.id.sortingEmail -> sortListByEmail()
            R.id.sortingLastAdded -> sortListByLastAdded()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity?.menuInflater?.inflate(R.menu.sorting_menu,menu)
    }
}