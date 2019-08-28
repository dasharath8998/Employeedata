package com.example.employeedata.activity

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.employeedata.fragment.EmployeeAddFragment
import com.example.employeedata.fragment.EmployeeListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import com.example.employeedata.R
import android.content.Context


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    var fMnager: FragmentManager? = null
    var toggle: ActionBarDrawerToggle? = null

    /*
    * OnCreate
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intialization()
    }

    /*
    * initializing fragmentManager variable
    * adding first fragment to frame layout
    * */
    fun intialization(){

        fMnager = supportFragmentManager
        var tx = fMnager?.beginTransaction()
        tx?.add(R.id.fragMain,EmployeeListFragment())
        tx?.commit()
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        toggle?.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(toggle!!)
        toggle!!.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }


    /*
    * if navigation drawer bar is visible than onBackpress will hide navigation drawer bar
    * */
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /*
    * OnNavigationItemSelected method
    * action on selecting specific menu item
    * */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.empList -> {
                var tx = fMnager?.beginTransaction()
                tx?.replace(R.id.fragMain,EmployeeListFragment())
                tx?.commit()
            }
            R.id.addEmp -> {
                var tx = fMnager?.beginTransaction()
                tx?.replace(R.id.fragMain,EmployeeAddFragment())
                tx?.addToBackStack(null)
                tx?.commit()
            }
            R.id.settings -> {

            }
            R.id.logout -> {

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    /*
    * For hiding keyboard
    * */
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val f = activity.currentFocus
        imm.hideSoftInputFromWindow(f?.windowToken, 0)
    }
}
