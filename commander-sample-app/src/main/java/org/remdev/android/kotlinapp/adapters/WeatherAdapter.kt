package org.remdev.android.kotlinapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.remdev.android.kotlinapp.R
import org.remdev.android.kotlinapp.models.WeatherItem
import org.remdev.android.kotlinapp.rest.findView

class WeatherAdapter (
        context: Context,
        private val items : List<WeatherItem>
) : RecyclerView.Adapter<WeatherAdapter.Companion.WeatherViewHolder>() {

    private val inflater : LayoutInflater = LayoutInflater.from(context);

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = items[position]
        holder.tempView.text = "${item.currentTemperature.toInt()}\u2103"
        holder.descView.text = item.weatherStateName
        holder.humidityView.text = "Hum: ${item.humidity}%"
        holder.timeView.text = item.applicableDate
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = WeatherViewHolder(inflater.inflate(R.layout.layout_day_weather, parent, false))

    companion object {
        class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            internal val tempView = view.findView<TextView>(R.id.tv_temp);
            internal val descView = view.findView<TextView>(R.id.tv_description);
            internal val humidityView = view.findView<TextView>(R.id.tv_humidity);
            internal val timeView = view.findView<TextView>(R.id.tv_time);
        }
    }
}