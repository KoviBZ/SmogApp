package com.smog.app.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.smog.app.R
import kotlinx.android.synthetic.main.view_error.view.*

class ErrorView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_error, this, true)
    }

    fun setErrorMessage(text: String) {
        error_content.text = text
    }

    fun setRetryAction(onClickListener: OnClickListener) {
        error_retry_button.setOnClickListener {
            onClickListener.onClick(it)
            it.isEnabled = false
        }
    }

    fun enableRetryButton() {
        error_retry_button.isEnabled = true
    }
}