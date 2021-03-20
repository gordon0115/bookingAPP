package com.fyp.pj.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.fyp.pj.R
import com.fyp.pj.objects.FragmentTags
import com.fyp.pj.view.base.BaseFragmentActivity
import com.fyp.pj.view.fragment.BookFragment
import com.fyp.pj.view.fragment.HomeFragment
import com.fyp.pj.view.fragment.FacilityFragment
import com.fyp.pj.view.fragment.MyBookFragment
import com.fyp.pj.widgets.MenuBar
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseFragmentActivity(), MenuBar.OnBottomMenuBarCallback {
    public var currentTab = 0
//    lateinit var context: Context
//
//    lateinit var mainActivityViewModel: MainActivityViewModel

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        context = this@MainActivity
//
//        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
//
//        btnClick.setOnClickListener {
//
//            wp7progressBar.showProgressBar()
//
//            mainActivityViewModel.getUser()!!.observe(this, Observer { serviceSetterGetter ->
//
//                wp7progressBar.hideProgressBar()
//
////                val msg = serviceSetterGetter.message
////
////                lblResponse.text = msg
//
//            })
//
//        }
//
//    }

    override fun getFragmentLayoutId(): Int {
        return R.id.mainContent
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restore(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("currentTab", currentTab)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            else Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onResume() {
        super.onResume()
        bottomBar?.setSelected(currentTab)
    }

    override fun onMenuClick(position: Int) {
        when (position) {
            0 -> getFragmentHelper().swap(0, HomeFragment.get(), FragmentTags.TAG_HOME)
            1 -> getFragmentHelper().swap(1, BookFragment.get(), FragmentTags.TAG_BOOK)
            2 -> getFragmentHelper().swap(2, FacilityFragment.get(), FragmentTags.TAG_MYBOOK)
            3 -> getFragmentHelper().swap(3, MyBookFragment.get(), FragmentTags.TAG_FACILITY)
        }

        currentTab = position
        bottomBar?.setSelected(position)
    }

    fun hideBottomBar(show : Boolean){
       if(show){
           bottomBar.visibility = View.GONE
       }else{
           bottomBar.visibility = View.VISIBLE
        Log.v(":",getString(R.string.alert_submit))
       }
    }

    private fun restore(savedInstanceState: Bundle?){
        savedInstanceState?.getInt("currentTab")?.also {
            currentTab = it
            bottomBar.setSelected(currentTab)
        } ?: run {
            onMenuClick(currentTab)
        }

        bottomBar?.setOnBottomMenuBarCallback(this)
    }
}
