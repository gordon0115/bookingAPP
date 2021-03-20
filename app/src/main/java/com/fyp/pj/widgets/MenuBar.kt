package com.fyp.pj.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.fyp.pj.R
import kotlinx.android.synthetic.main.layout_bar_item.view.*

class MenuBar : FrameLayout {

    private var onBottomMenuBarCallback: OnBottomMenuBarCallback? = null
    private var layoutId = -1

    interface OnBottomMenuBarCallback {
        fun onMenuClick(position: Int)
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {
        clipChildren = false
        clipToPadding = false

        attrs?.also {
            val typeArr = context.obtainStyledAttributes(attrs, R.styleable.BarItem)
            layoutId = typeArr.getResourceId(R.styleable.BarItem_menuLayoutId, -1)
            LayoutInflater.from(context).inflate(layoutId, this)
            create()
        }
    }

    fun setOnBottomMenuBarCallback(callback: OnBottomMenuBarCallback) {
        onBottomMenuBarCallback = callback
    }

    private fun create(){
        getChildAt(0)?.also { wrapper ->
            if (wrapper is ViewGroup) {
                for (i in 0 until wrapper.childCount) {
                    val barItem = wrapper.getChildAt(i)
                    if(barItem is BarItem) {
                        barItem.setOnClickListener {
                            onBottomMenuBarCallback?.onMenuClick(i)
                        }
                    }
                }
            }
        }
    }


    fun setSelected(position: Int) {
        getChildAt(0)?.also { view ->
            if (view is LinearLayout) {
                for (i in 0 until view.childCount) {
                    val barItem = view.getChildAt(i)
                    if (barItem is BarItem) {
                        barItem.setItemSelected(position == i)
                    }
                }
            }
        }
    }

    class BarItem : FrameLayout {

        private var iconOff: Drawable? = null
        private var iconOn: Drawable? = null

        private var textColorSelected: Int = Color.RED
        private var textColorDefault: Int = Color.BLACK

        private var lottieJson: Int = -1

        constructor(context: Context) : super(context) {
            init(null)
        }

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            init(attrs)
        }

        private fun init(attrs: AttributeSet?) {
            clipChildren = false
            clipToPadding = false

            LayoutInflater.from(context).inflate(R.layout.layout_bar_item, this)

            attrs?.also {
                val typeArr = context.obtainStyledAttributes(attrs, R.styleable.BarItem)
                try {
                    iconOff = typeArr.getDrawable(R.styleable.BarItem_iconDrawableOff)
                    iconOn = typeArr.getDrawable(R.styleable.BarItem_iconDrawableOn)

                    lottieJson = typeArr.getResourceId(R.styleable.BarItem_lottieRaw, -1)
                    lottieJson.takeIf { it > -1 }?.also { json ->
                        laAnimView?.also {
                            laAnimView.visibility = View.VISIBLE
                            laAnimView.setAnimation(lottieJson)
                        }
                    }

                    val tvDefaultHexColor = typeArr.getString(R.styleable.BarItem_textColorDefault)
                    val tvSelectedHexColor = typeArr.getString(R.styleable.BarItem_textColorSelected)

                    textColorDefault = tvDefaultHexColor?.let {
                        try {
                            Color.parseColor(tvDefaultHexColor)
                        } catch (e: Exception) {
                            Color.BLACK
                        }
                    } ?: Color.BLACK

                    textColorSelected = tvDefaultHexColor?.let {
                        try {
                            Color.parseColor(tvSelectedHexColor)
                        } catch (e: Exception) {
                            Color.RED
                        }
                    } ?: Color.RED

                    ivIcon.setImageDrawable(iconOff)
                    typeArr.getString(R.styleable.BarItem_text)?.also { text ->
                        tvTitle.text = text
                        tvTitle.visibility = View.VISIBLE
                        tvTitle.setTextColor(textColorDefault)
                    }

                } finally {
                    typeArr.recycle()
                }
            }
        }

        private fun setIconSelected(isSelected: Boolean){
            if (isSelected) {
                ivIcon.setImageDrawable(iconOn)
                tvTitle.setTextColor(textColorSelected)

                ivIcon.visibility = View.GONE
            }else{
                ivIcon.setImageDrawable(iconOff)
                tvTitle.setTextColor(textColorDefault)

                ivIcon.visibility = View.VISIBLE
            }
        }

        private fun setLottieSelected(isSelected: Boolean){
            if(isSelected){
                laAnimView.playAnimation()
                tvTitle.setTextColor(textColorSelected)
            }else{
                laAnimView.cancelAnimation()
                laAnimView.progress = 0f
                tvTitle.setTextColor(textColorDefault)
            }
        }

        fun setItemSelected(isSelected: Boolean) {
            if(iconOff != null && iconOn != null){
                setIconSelected(isSelected)
            }else{
                setLottieSelected(isSelected)
            }
        }

    }
}