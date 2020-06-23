package com.smog.app.ui.citylist.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smog.app.R
import com.smog.app.utils.OnItemClickListener

class CityAdapter(
    private val context: Context,
    private val items: List<String>,
    private val onItemClickListener: OnItemClickListener<String>
): RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_city, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class CityViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val cityName = view.findViewById<TextView>(R.id.city_name)

        fun bind(name: String) {
            cityName.setOnClickListener {
                onItemClickListener.onItemClick(name)
            }
            cityName.text = name
        }
    }
}