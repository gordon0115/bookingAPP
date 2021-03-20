package com.fyp.pj.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fyp.pj.R
import com.fyp.pj.adapter.NewsAdapter

class HomeFragment:  Fragment()  {

    var tvTitle : TextView? = null
    var rlNews : RecyclerView? = null
        companion object{
        fun get(): HomeFragment{
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rlNews  =  view .findViewById(R.id.rlNews)
        tvTitle = view.findViewById(R.id.tvTitle)
        initList()
    }

    fun initList(){
        val list = arrayListOf<String>()
        list.add("vvv")
        list.add("vvv")
        list.add("vvv")
        list.add("vvv")
        rlNews?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = NewsAdapter(list)
        }
    }
}