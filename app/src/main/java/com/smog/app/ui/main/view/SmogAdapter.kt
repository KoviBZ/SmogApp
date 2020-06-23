package com.smog.app.ui.main.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smog.app.R
import com.smog.app.network.dto.SensorData

class SmogAdapter(
    private val context: Context,
    private val items: List<SensorData>
) : RecyclerView.Adapter<SmogAdapter.SmogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmogViewHolder {
        return SmogViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_details, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SmogViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class SmogViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val smogComponent = view.findViewById<TextView>(R.id.smog_component)
        private val smogDensity = view.findViewById<TextView>(R.id.smog_density)

        fun bind(sensorData: SensorData) {
            if (sensorData.values.isNotEmpty()) {
                smogComponent.text = String.format(
                    context.getString(R.string.density_label_success),
                    sensorData.key,
                    sensorData.values[0].date
                )
                smogDensity.text = "${sensorData.values[0].value}"
            } else {
                smogComponent.text = context.getString(R.string.density_label_success)
            }
        }
    }
}