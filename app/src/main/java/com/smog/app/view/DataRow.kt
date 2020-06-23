package com.smog.app.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.smog.app.R
import kotlinx.android.synthetic.main.view_data_row.view.*

class DataRow(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_data_row, this, true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DataRow)
        val labelText = typedArray.getString(R.styleable.DataRow_label) ?: ""

        row_label.text = labelText

        typedArray.recycle()
    }

    fun setValue(text: String) {
        row_value.text = text
    }
}