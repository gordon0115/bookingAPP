package com.fyp.pj.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.fyp.pj.R

class FacilityFragment:  Fragment()  {

    var tvTitle : TextView? = null
    var rlNews : RecyclerView? = null
        companion object{
        fun get(): FacilityFragment{
            return FacilityFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_facility, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rlNews  =  view .findViewById(R.id.rlNews)
        tvTitle = view.findViewById(R.id.tvTitle)
        initList()
    }

    fun initList(){

    }
}