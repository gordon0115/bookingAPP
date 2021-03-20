package com.fyp.pj.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.fyp.pj.R

class MyBookFragment:  Fragment()  {

    var tvTitle : TextView? = null
    var rlMyBook : RecyclerView? = null
        companion object{
        fun get(): MyBookFragment{
            return MyBookFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_mybook, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rlMyBook  =  view .findViewById(R.id.rlMyBook)
        tvTitle = view.findViewById(R.id.tvTitle)
        initList()
    }

    fun initList(){

    }
}