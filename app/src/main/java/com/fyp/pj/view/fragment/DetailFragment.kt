package com.fyp.pj.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.fyp.pj.R
import com.fyp.pj.view.activity.CaptureActivity
import com.fyp.pj.view.activity.MainActivity
import com.fyp.pj.view.base.BaseFragment
import com.google.zxing.integration.android.IntentIntegrator

class DetailFragment:  BaseFragment()  {

    var tvContent : TextView? = null
    var rlNews : RecyclerView? = null

    var btnScan : AppCompatButton? = null
    var btnHome : AppCompatButton? = null

        companion object{
        fun get(): DetailFragment{
            return DetailFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_detail
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)?.hideBottomBar(true)
        rlNews  =  view .findViewById(R.id.rlNews)
        tvContent = view.findViewById(R.id.tvContent)
        btnHome = view.findViewById(R.id.btnCancel)
        btnScan = view.findViewById(R.id.btnScan)
        initList()
    }

    fun initList(){
        tvContent?.text   = "---"
        btnHome?.setOnClickListener {
            activity?.onBackPressed()
        }
        btnScan?.setOnClickListener {
            scanQRCode()
        }
    }

    private fun scanQRCode(){
        val integrator = IntentIntegrator(activity).apply {
            captureActivity = CaptureActivity::class.java
            setOrientationLocked(false)
            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            setPrompt("Scanning Code")
        }
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            else Toast.makeText(context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity?)?.hideBottomBar(false)
    }
}