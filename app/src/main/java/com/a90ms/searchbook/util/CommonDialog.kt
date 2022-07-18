package com.a90ms.searchbook.util

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.Dimension
import androidx.appcompat.app.AppCompatDialog
import androidx.core.view.isVisible
import com.a90ms.searchbook.R
import com.a90ms.searchbook.base.BaseDialog
import com.a90ms.searchbook.base.bindSingleClick
import com.a90ms.searchbook.databinding.DialogCommonBinding

fun Context?.showCommonDialog(
    title: String? = null,
    message: String? = null,
    positive: String,
    negative: String? = null,
    isCancelable: Boolean = true,
    isButtonClickDismiss: Boolean = true,
    listener: ((dialog: Dialog, which: Int) -> Unit)? = null
): AppCompatDialog? = this?.let {
    BaseDialog<DialogCommonBinding>(
        it,
        R.layout.dialog_common,
        enableDim = true
    ).onShow { binding ->
        setCancelable(isCancelable)

        binding.tvDialogTitle.run {
            this.isVisible = title != null
            title?.let { text = it }
        }

        binding.tvDialogContent.run {
            this.isVisible = message != null
            message?.let { this.text = it }
        }

        binding.tvDialogPositive.run {
            text = positive
            setTextSize(Dimension.SP, 14f)
            bindSingleClick {
                listener?.invoke(this@onShow, DialogInterface.BUTTON_POSITIVE)
                if (isButtonClickDismiss) {
                    dismiss()
                }
            }
        }

        with(binding.tvDialogNegative) {
            isVisible = negative != null

            negative?.let {
                text = negative
                setTextSize(Dimension.SP, 14f)
                bindSingleClick {
                    listener?.invoke(this@onShow, DialogInterface.BUTTON_NEGATIVE)
                    if (isButtonClickDismiss) {
                        dismiss()
                    }
                }
            }
        }

        binding.viewLine2.isVisible = binding.tvDialogNegative.isVisible
    }
}?.also { it.show() }