package com.a90ms.searchbook.base

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.a90ms.searchbook.R
import com.a90ms.searchbook.util.OnSingleClickListener
import com.a90ms.searchbook.util.isValidContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

@BindingAdapter("bindVisible")
fun View.bindVisible(show: Boolean?) {
    isVisible = show ?: false
}

@SuppressLint("CheckResult")
@BindingAdapter(
    value = [
        "bindImage",
        "bindPlaceHolder",
        "bindViewModel"
    ],
    requireAll = false
)
fun ImageView.bindImage(
    url: String?,
    placeHolder: Drawable? = null,
    baseViewModel: BaseViewModel?
) {
    if (context.isValidContext()) {
        val glide = Glide.with(this)
            .load(url)
            .fitCenter()
            .placeholder(placeHolder)
            .error(placeHolder)

        if (baseViewModel != null) {
            baseViewModel.showLoading()
            glide.addListener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        baseViewModel.hideLoading()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        baseViewModel.hideLoading()
                        return false
                    }
                }
            )
        }

        glide.into(this)
    }
}

@BindingAdapter("bindSingleClick")
fun View.bindSingleClick(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

@BindingAdapter("bindListToString")
fun TextView.bindListToString(list: List<String?>?) {
    text = list?.joinToString(separator = ", ") { it!! }
}

@BindingAdapter(
    value = [
        "bindIntValue",
        "bindUnit",
        "bindPrefix"
    ],
    requireAll = false
)
fun TextView.bindNumberToString(value: Int?, unit: String, isPrefix: Boolean? = false) {
    if (value == null) {
        isVisible = false;
        return
    }
    text = if (isPrefix == true) {
        "$unit$value"
    } else {
        "$value$unit"
    }
}

@BindingAdapter(
    value = [
        "bindTextEmptyCheck",
        "bindIsLink"
    ],
    requireAll = false
)
fun TextView.bindTextEmptyCheck(value: String?, isLink: Boolean? = false) {
    val linkColor = ContextCompat.getColor(context, R.color.purple_700)

    val str = if (value.isNullOrEmpty()) {
        "정보가 없습니다."
    } else {
        value
    }
    if (value.isNullOrEmpty().not() && isLink == true) {
        setTextColor(linkColor)
    }

    text = str
}