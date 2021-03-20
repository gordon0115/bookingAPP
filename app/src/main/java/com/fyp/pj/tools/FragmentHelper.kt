package com.fyp.pj.tools

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.fyp.pj.R

class FragmentHelper(fm: FragmentManager, frameId: Int = -1, moveNextStock: Boolean = false) {

    private var mFragmentMgr: FragmentManager = fm
    private var mFragmentInfo: FragmentInfo = FragmentInfo()

    private var mAnimEnter = R.anim.fade_in
    private var mAnimExit = R.anim.fade_out

    init {
        mFragmentInfo.frameId = frameId
        mFragmentInfo.moveNextStock = moveNextStock
        mFragmentInfo.fragmentStack.add(0)
    }

    interface OnFragmentInterface {
        fun isBackBlock(): Boolean
    }

    fun setFragmentInfo(fragmentInfo: FragmentInfo) {
        mFragmentInfo = fragmentInfo
    }


    fun setFrameId(frameId: Int) {
        mFragmentInfo.frameId = frameId
    }

    fun getFragmentInfo(): FragmentInfo {
        return mFragmentInfo
    }


    fun findByTag(tag: String): Fragment? {
        return mFragmentMgr.findFragmentByTag(tag)
    }

    fun add(frameId: Int, fragment: Fragment, tag: String) {
        if (frameId != mFragmentInfo.frameId) {
            val ft = mFragmentMgr.beginTransaction()
            ft.add(frameId, fragment, tag).commit()
        }
    }

    fun replace(frameId: Int, fragment: Fragment, tag: String) {
        if (frameId != mFragmentInfo.frameId) {
            val ft = mFragmentMgr.beginTransaction()
            ft.replace(frameId, fragment, tag).commit()
        }
    }

    fun replace(fragment: Fragment?, tag: String) {
        fragment?.also {
            val ft = mFragmentMgr.beginTransaction()
            if (mFragmentInfo.isTransAnim) {
                ft.setCustomAnimations(mAnimEnter, mAnimExit)
            }

            val curTags = mFragmentInfo.fragmentMaps[mFragmentInfo.curPosition] ?: arrayListOf()
            val tags = arrayListOf<String>()
            tags.addAll(curTags)

            curTags.forEach { t ->
                if (t != tag) {
                    mFragmentMgr.findFragmentByTag(t)?.also { f ->
                        ft.remove(f)
                        tags.remove(t)
                    }
                }
            }

            if (tags.isEmpty()) {
                ft.add(mFragmentInfo.frameId, fragment, tag)
            } else {
                val targetFragment = mFragmentMgr.findFragmentByTag(tag)
                if (targetFragment != null) {
                    ft.show(targetFragment)
                } else {
                    ft.add(mFragmentInfo.frameId, fragment, tag)
                }
            }

            mFragmentInfo.curTag = tag
            mFragmentInfo.fragmentMaps.put(mFragmentInfo.curPosition, curTags.apply {
                clear()
                add(tag)
            })

            ft.commit()
        }
    }

    fun add(fragment: Fragment?, tag: String) = apply {
        fragment?.also {
            val ft = mFragmentMgr.beginTransaction()
            if (mFragmentInfo.isTransAnim) {
                ft.setCustomAnimations(mAnimEnter, mAnimExit)
            }

            val targetFragment = mFragmentMgr.findFragmentByTag(tag)
            val curFragment = mFragmentMgr.findFragmentByTag(mFragmentInfo.curTag)
            val curTags = mFragmentInfo.fragmentMaps.get(mFragmentInfo.curPosition) ?: arrayListOf()

            curFragment?.also { f ->
                ft.hide(f)
            }

            if (targetFragment != null) {
                ft.show(targetFragment)
            } else {
                ft.add(mFragmentInfo.frameId, fragment, tag)
            }

            ft.commit()

            mFragmentInfo.curTag = tag
            mFragmentInfo.fragmentMaps.put(mFragmentInfo.curPosition, curTags.apply {
                add(tag)
            })
        }
    }

    fun swap(position: Int, fragment: Fragment?, tag: String) {
        fragment ?: return

        if (!mFragmentInfo.moveNextStock) {
            mFragmentInfo.fragmentStack.clear()
            mFragmentInfo.fragmentStack.add(position)
        }

        mFragmentMgr.beginTransaction().apply {
            mFragmentMgr.fragments.forEach { hide(it) }
            commit()
        }

        if (position != mFragmentInfo.curPosition) {
            val ft = mFragmentMgr.beginTransaction()
            if (mFragmentInfo.isTransAnim) {
                ft.setCustomAnimations(mAnimEnter, mAnimExit)
            }

            val curTags = mFragmentInfo.fragmentMaps.get(position) ?: arrayListOf()

            if(curTags.size == 0){
                ft.add(mFragmentInfo.frameId, fragment, tag)
                curTags.add(tag)
                mFragmentInfo.curTag = tag
            }else{
                curTags.lastOrNull()?.also { oldTag ->
                    mFragmentMgr.findFragmentByTag(oldTag)?.also {
                        ft.show(it)
                    } ?: run {
                        ft.add(mFragmentInfo.frameId, fragment, oldTag)
                        curTags.add(oldTag)
                    }

                    mFragmentInfo.curTag = oldTag
                }

            }

            mFragmentInfo.curPosition = position
            mFragmentInfo.fragmentStack.add(position)
            mFragmentInfo.fragmentStack.last().also { index -> index == position }
            mFragmentInfo.fragmentMaps.put(position, curTags)

            ft.commit()
        } else {
            replace(fragment, tag)
        }
    }

    /**
     * when it return true which activity will use super.onBackPressed()
     * @return boolean
     */
    fun pop(): Boolean {
        val ft = mFragmentMgr.beginTransaction()
        if (mFragmentInfo.isTransAnim) {
            ft.setCustomAnimations(mAnimEnter, mAnimExit)
        }

        val curTags = mFragmentInfo.fragmentMaps.get(mFragmentInfo.curPosition) ?: arrayListOf()
        if (curTags.size == 1 && mFragmentInfo.fragmentStack.size > 1) {

            curTags.remove(mFragmentInfo.curTag)

            val curFragment = mFragmentMgr.findFragmentByTag(mFragmentInfo.curTag)
            curFragment?.also {
                if (!containsTag(mFragmentInfo.curPosition, mFragmentInfo.curTag)) {
                    ft.remove(it)
                } else {
                    ft.hide(it)
                }
            }

            mFragmentInfo.fragmentMaps.put(mFragmentInfo.curPosition, curTags)
            mFragmentInfo.fragmentStack.also {
                if (it.size > 0) {
                    it.remove(mFragmentInfo.curPosition)
                    mFragmentInfo.curPosition = it.last()
                }
            }

            mFragmentInfo.fragmentMaps.get(mFragmentInfo.curPosition)?.lastOrNull()?.also { tag ->
                val targetFragment = mFragmentMgr.findFragmentByTag(tag)
                if (targetFragment != null) {
                    ft.show(targetFragment)
                }

                mFragmentInfo.curTag = tag
            }

            ft.commit()

            return true
        } else if (curTags.size > 1) {

            curTags.remove(mFragmentInfo.curTag)

            val curFragment = mFragmentMgr.findFragmentByTag(mFragmentInfo.curTag)
            curFragment?.also { f ->
                if (!containsTag(mFragmentInfo.curPosition, mFragmentInfo.curTag)) {
                    ft.remove(f)
                } else {
                    ft.hide(f)
                }
            }

            curTags.last().also { tag ->
                val targetFragment = mFragmentMgr.findFragmentByTag(tag)
                if (targetFragment != null) {
                    ft.show(targetFragment)
                }

                mFragmentInfo.curTag = tag
            }

            ft.commit()
            mFragmentInfo.fragmentMaps.put(mFragmentInfo.curPosition, curTags)

            return curTags.size == 1 && (!mFragmentInfo.moveNextStock || mFragmentInfo.fragmentStack.size > 1)
        }

        return false
    }

    fun isBackBlock(): Boolean {
        val curFragment = findByTag(mFragmentInfo.curTag)
        curFragment?.also {
            if (it is OnFragmentInterface) {
                if (!it.isBackBlock()) {
                    return pop()
                } else {
                    return true
                }
            }
        }

        return false
    }

    fun getCurrentPosition(): Int {
        return mFragmentInfo.curPosition
    }

    private fun containsTag(position: Int, tag: String): Boolean {
        mFragmentInfo.fragmentMaps.forEach { (index, tags) ->
            tags.forEach {
                if (position != index && it.equals(tag)) {
                    return true
                }
            }
        }

        return mFragmentInfo.fragmentMainTags.contains(tag)
    }

    class FragmentInfo() : Parcelable {

        var fragmentMaps: HashMap<Int, ArrayList<String>> = hashMapOf()
        var fragmentStack: HashSet<Int> = hashSetOf()
        var fragmentMainTags: Array<String> = arrayOf()

        var moveNextStock = false
        var frameId = -1
        var curTag = ""
        var curPosition = 0
        var isTransAnim = false

        constructor(parcel: Parcel) : this() {
            fragmentMainTags = parcel.createStringArray() ?: arrayOf()
            moveNextStock = parcel.readByte() != 0.toByte()
            frameId = parcel.readInt()
            curTag = parcel.readString() ?: ""
            curPosition = parcel.readInt()
            isTransAnim = parcel.readByte() != 0.toByte()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeStringArray(fragmentMainTags)
            parcel.writeByte(if (moveNextStock) 1 else 0)
            parcel.writeInt(frameId)
            parcel.writeString(curTag)
            parcel.writeInt(curPosition)
            parcel.writeByte(if (isTransAnim) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<FragmentInfo> {
            override fun createFromParcel(parcel: Parcel): FragmentInfo {
                return FragmentInfo(parcel)
            }

            override fun newArray(size: Int): Array<FragmentInfo?> {
                return arrayOfNulls(size)
            }
        }

    }
}