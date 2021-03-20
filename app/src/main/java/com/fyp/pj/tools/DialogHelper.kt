package com.fyp.pj.tools

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import com.fyp.pj.R
import com.fyp.pj.databinding.DialogMessageBinding

class DialogHelper(private val context: Context?) {

    private var dialog: Dialog? = null
    private var contentView: View? = null
    private var isCancelable: Boolean = false

    private fun build(@LayoutRes layoutId: Int) = apply {
        dialog?.dismiss()
        context?.also {
            if(dialog == null){
                dialog = Dialog(it, R.style.FullScreenDialog)
            }
            contentView = LayoutInflater.from(it).inflate(layoutId, null).also {
                dialog?.setContentView(it)
            }
        }
    }

    fun show() = apply {
        dialog?.also {
            if (it.isShowing) {
                it.dismiss()
            }
            it.setCancelable(isCancelable)
            it.show()
        }
    }

    private fun getContentView(): View? {
        return contentView
    }


    fun dismiss() {
        dialog?.also {
            it.dismiss()
        }
    }

    fun showMessage(message: String?, ok: (() -> Unit?)? = { dismiss() }, cancel: (() -> Unit?)? = { dismiss() }){
        build(R.layout.dialog_message).also { helper ->
            helper.getContentView()?.also { view ->
                val binding = DataBindingUtil.bind<DialogMessageBinding>(view)
                message?.also {
                    binding?.tvMessage?.text = it
                    binding?.tvMessage?.visibility = View.VISIBLE
                }

                binding?.btnOK?.setOnClickListener {
                    ok?.also {
                        dismiss()
                        ok()
                    }
                }
                binding?.btnCancel?.setOnClickListener {
                    cancel?.also {
                        dismiss()
                        cancel()
                    }
                }
            }
        }.show()
    }

}