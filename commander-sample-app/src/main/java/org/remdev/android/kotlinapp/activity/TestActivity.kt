package org.remdev.android.kotlinapp.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import org.remdev.android.kotlinapp.R
import org.remdev.android.kotlinapp.activity.presenters.WeatherPresenter
import org.remdev.android.kotlinapp.adapters.WeatherAdapter
import org.remdev.android.kotlinapp.models.WeatherItem
import org.remdev.android.kotlinapp.rest.findView

class TestActivity : BaseKotlinAppActivity(), WeatherPresenter.WeatherPresenterCallback {

    private val loadBtn : Button by lazy { findView<Button>(R.id.btn_load) }
    private val cityNameView : EditText by lazy { findView<EditText>(R.id.et_location) }
    private val weatherList : RecyclerView by lazy { findView<RecyclerView>(R.id.rv_weather_list) }
    private lateinit var presenter : WeatherPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        presenter = WeatherPresenter(this, this)

        weatherList.layoutManager = LinearLayoutManager(TestActivity@ this, LinearLayoutManager.VERTICAL, false)
        initListeners()
    }

    private fun initListeners() {
        loadBtn.setOnClickListener {
            val city = cityNameView.text.toString().trim()
            if (city.isBlank()) {
                cityNameView.error = getString(R.string.enter_city_name)
                return@setOnClickListener;
            }
            presenter.loadWeather(city)
        }

        cityNameView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cityNameView.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onLoaded(data: List<WeatherItem>) {
        weatherList.adapter = WeatherAdapter(this, data)
    }
}
