package com.fyp.pj.view.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.fyp.pj.R
import com.fyp.pj.tools.DialogHelper
import org.angmarch.views.NiceSpinner
import java.text.SimpleDateFormat
import java.util.*


class BookFragment:  Fragment()  {

    var tvTitle : TextView? = null
    var spDistract : NiceSpinner? = null
    var tvClickableDate : TextView? = null
    var spType : NiceSpinner? = null
    var spTime : NiceSpinner? = null
    var distractList : MutableList<String>? = null
    var typeList : List<String>? = null
    var timetList : List<String>? = null
    var btnSubmit : AppCompatButton? = null
    private var dialogHelper: DialogHelper? = null

    var distract : String? = ""
    var type : String? = ""
    var time: String? = ""

    var cal = Calendar.getInstance()

        companion object{
        fun get(): BookFragment{
            return BookFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_book, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spDistract = view.findViewById(R.id.spDistract)
        spType = view.findViewById(R.id.spType)
        spTime = view.findViewById(R.id.spTime)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        dialogHelper = DialogHelper(context)
        tvClickableDate = view.findViewById(R.id.tvClickableDate)

        initList()
    }

    fun initList(){
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

       distractList  = LinkedList(listOf("One", "Two", "Three", "Four", "Five"))
        typeList  = LinkedList(Arrays.asList("One", "Two", "Three", "Four", "Five"))
        timetList  = LinkedList(Arrays.asList("One", "Two", "Three", "Four", "Five"))

        spDistract?.attachDataSource(distractList as List<String>)
        spType?.attachDataSource(typeList as List<String>)
        spTime?.attachDataSource(timetList as List<String>)

        spDistract?.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            distract = (distractList as LinkedList<String>)[position] 
        }

        spType?.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            type = (typeList as LinkedList<String>)[position]
        }

        spTime?.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            time = (timetList as LinkedList<String>)[position]
        }

        btnSubmit?.setOnClickListener {
            dialogHelper?.showMessage(getString(R.string.alert_submit), ok = {
                openDetail()
            })
        }

        tvClickableDate!!.setOnClickListener {
            context?.let {
                val diaglog = DatePickerDialog(
                    it,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                diaglog.datePicker.minDate = cal.timeInMillis
                diaglog.show()
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tvClickableDate!!.text = sdf.format(cal.getTime())
    }

    fun openDetail( ){
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(
                R.id.mainContent,
                DetailFragment.get(), DetailFragment.toString()
            )
            ?.addToBackStack(null) ?.commit()
    }
}