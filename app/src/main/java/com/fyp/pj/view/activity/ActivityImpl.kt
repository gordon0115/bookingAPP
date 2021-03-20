package com.fyp.pj.view.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fyp.pj.delegate.OnActivityDelegate
import com.fyp.pj.delegate.OnDataBindingDelegate
import com.fyp.pj.delegate.OnViewModelDelegate
import com.fyp.pj.retrofit.WebThrowable
import com.fyp.pj.tools.DialogHelper

abstract class ActivityImpl : AppCompatActivity(), OnActivityDelegate, OnViewModelDelegate {

    protected val apiErrorHandler = MutableLiveData<Throwable>()
    protected val apiLoadingHandler = MutableLiveData<Boolean>()

    private lateinit var loadingDialog: ProgressDialog
    private var binding: ViewDataBinding? = null
    private var dialogHelper: DialogHelper? = null


    abstract fun getLayoutId(): Int

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getLayoutId() > 0) {
            setContentView(this, getLayoutId())
        }

        dialogHelper = DialogHelper(this)

        loadingDialog = ProgressDialog(this)
        loadingDialog.setMessage("Loading")

        apiLoadingHandler.observe(this, Observer { showing ->
            loadingDialog?.dismiss()
            if(showing){
                loadingDialog?.show()
            }
        })

        apiErrorHandler.observe(this, Observer { throwable ->
                if (throwable is WebThrowable) {
                    showMessage(message = throwable.getErrorMessage())
                }
            })

    }

    fun setContentView(context: Context, @LayoutRes layoutId: Int) {
        if (context is OnDataBindingDelegate<*>) {
            binding = context.onCreateDataBinding(DataBindingUtil.setContentView(this, layoutId))
            binding?.lifecycleOwner = this
        } else {
            setContentView(layoutId)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        loadingDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.unbind()
    }

    override fun whenError(throwable: Throwable) {
        apiErrorHandler.value = throwable
    }


    override fun whenLoading(showLoading: Boolean) {
        apiLoadingHandler.value = showLoading
    }


    override fun showProgress(display: Boolean) {
        if (display) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
    }

    override fun showMessage(type: Int, title: String?, message: String?, params: Intent?) {
        dialogHelper?.showMessage(message)
    }

    override fun go(intent: Intent, killBefore: Boolean) {
        startActivity(intent)
        if (killBefore) finish()
        overridePendingTransition(0, 0)
    }

    override fun goForResult(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
        overridePendingTransition(0, 0)
    }

    override fun goSingleTop(intent: Intent) {
        if (Build.VERSION.SDK_INT >= 11) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        } else {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}