package com.example.employeedata.fragment


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.bumptech.glide.Glide
import com.example.employeedata.R
import com.example.employeedata.database.EmployeeDatabase
import com.example.employeedata.listeners.DateSetListener
import com.example.employeedata.model.Employee
import com.example.employeedata.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.employee_add.*
import java.util.*

class EmployeeAddFragment: Fragment() {

    var uri: String = ""
    var db: EmployeeDatabase? = null
    var aweVal: AwesomeValidation? = null
    var eId: Long? = null

    var cYear = 0
    var cMonth = 0
    var cDay = 0

    /*
    *initializing basic needed variables
    */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        db = EmployeeDatabase.getInstance(context!!)
        aweVal = AwesomeValidation(ValidationStyle.BASIC)
        eId = arguments?.getLong(Utils.EMPLOYEE_ID)
        val c = Calendar.getInstance()
        cYear = c.get(Calendar.YEAR)
        cMonth = c.get(Calendar.MONTH)
        cDay = c.get(Calendar.DAY_OF_MONTH)

        return inflater.inflate(R.layout.employee_add,container,false)

    }

    /*
    * setting data as per EID sent through Recycler on click event
    * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.invalidateOptionsMenu()

        listeners()
        if(eId != null)
            getDataFromDb()
    }

    /*
    * listeners for buttons and images
    * */
    fun listeners(){

        btnSubmit.setOnClickListener {
            if(eId == null)
                insertDataToDB()
            else
                updateDataToDb()
        }

        imgEdit.setOnClickListener {
            checkPermissionForReadData()
        }

        btnCalender.setOnClickListener {
            openCalendar()
        }

        btnReset.setOnClickListener {
            resetForm()
        }

        etBirthday.setOnClickListener {
            openCalendar()
        }

    }

    /*
    * run if eid not null and getting data fro that particular eid data and set to form
    * */
    fun getDataFromDb(){

        lockNavigationDrawer()

        Thread(Runnable {
            kotlin.run {

                val employee = db?.employeeDao()?.getEmployee(eId!!)

                activity?.runOnUiThread{
                    Glide.with(context!!).load(employee?.img).placeholder(R.drawable.ic_account_circle_black_24dp).into(imgProfile)
                    etName.setText(employee?.name)
                    etEmail.setText(employee?.email)
                    etBirthday.setText(employee?.birthdate)
                    tvYear.setText(Utils.stringToDate(employee?.birthdate!!,"dd, mm, yyyy"))

                    if(employee?.gender == "Male")
                        rBMale.isChecked = true
                    else
                        rBFemale.isChecked = true

                    btnSubmit.setText("Update")
                }
            }
        }).start()
    }

    /*
    * run if eid is null and insert data for employee
    * */
    fun insertDataToDB(){

        val gender = selectedRadioButton()
        var now = System.currentTimeMillis()
        val employee = Employee(id = null,img = uri,name = etName.text.toString(),email = etEmail.text.toString(),birthdate = etBirthday.text.toString(),gender = gender?.text.toString(),time = now)

        if(checkValidation()){
            if(etBirthday.text == "Birth date"){
                birthdayValidator()
            }else{
                Thread(Runnable {
                    kotlin.run {
                        db?.employeeDao()?.insetEmployee(employee)
                        activity?.runOnUiThread {
                            activity?.supportFragmentManager?.popBackStack()
                        }
                    }
                }).start()
            }
        }
    }

    /*
    * run if eid not null and update the data of particular employee
    * */
    fun updateDataToDb(){

        val gender = selectedRadioButton()

        if(checkValidation()){
            Thread(Runnable {
                kotlin.run {
                    val result = db?.employeeDao()?.updateEmployee(eId!!,uri,etName.text.toString(),etEmail.text.toString(),etBirthday.text.toString(),gender?.text.toString(),System.currentTimeMillis())
                    activity?.runOnUiThread {
                        if(result == 0){
                            activity?.runOnUiThread {
                                Toast.makeText(context,"Updation failed...",Toast.LENGTH_LONG).show()
                            }
                        }else{
                            activity?.supportFragmentManager?.popBackStackImmediate()
                            activity?.runOnUiThread {
                                Toast.makeText(context,"Updation Completed...",Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }).start()
        }
    }

    /*
    * lock navigation drawer set icon to back button
    * setting back button on click listener
    * */
    private fun lockNavigationDrawer() {
        activity?.drawer_layout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        activity?.toolbar?.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        activity?.toolbar?.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    /*
    * open date picker dialog if birthday text field if empty
    * */
    fun birthdayValidator(){

        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Message")
        dialog.setMessage("Birth date not selected from calendar")
        dialog.setPositiveButton("Open", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                openCalendar()
            }
        }).setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0?.cancel()
            }
        }).show()

    }

    fun openCalendar(){
        Utils.openCalender(context!!,cYear,cMonth,cDay, object : DateSetListener {
            override fun onDateSelected(year: Int, month: Int, day: Int) {
                cYear=year
                cMonth=month
                cDay=day
                etBirthday.setText("${day}, ${month+1}, ${year}")
                tvYear.text=Utils.getAge(year,month,day)
            }
        })
    }

    fun setImage(){
        val i = Intent()
        i.action = Intent.ACTION_GET_CONTENT
        i.type = "image/*"
        startActivityForResult(i,Utils.REQUEST_CODE_READ_DATA)
    }

    /*
    * check permission for access of internal storage is there or not
    * */
    private fun checkPermissionForReadData() {
        val status = ContextCompat.checkSelfPermission(context!!,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (status == PackageManager.PERMISSION_GRANTED)
            setImage()
        else
            ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),Utils.START_ACTIVITY_FOR_IMGE)
    }

    /*
    * checking editext validation returning boolean value
    * */
    fun checkValidation(): Boolean{
        aweVal?.addValidation(activity,R.id.etName,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$",R.string.name_error)
        aweVal?.addValidation(activity,R.id.etEmail, Patterns.EMAIL_ADDRESS,R.string.email_error)
        return aweVal?.validate()!!
    }

    /*
    * returning checked radio button id
    * */
    fun selectedRadioButton(): RadioButton?{
        val selId = rGroup.checkedRadioButtonId
        return view?.findViewById(selId)
    }

    fun resetForm(){
        Glide.with(context!!).load(R.drawable.ic_account_circle_black_24dp).into(imgProfile)
        etName.setText("")
        etEmail.setText("")
        etBirthday.setText("Birthday")
        tvYear.setText("Years:")
        rBMale.isChecked = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            setImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Utils.REQUEST_CODE_READ_DATA && data != null){
            uri = data.data.toString()
            Glide.with(context!!).load(uri).into(imgProfile)
        }
    }
}
