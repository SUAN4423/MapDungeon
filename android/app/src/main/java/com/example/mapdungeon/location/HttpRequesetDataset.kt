package com.example.mapdungeon.location

import android.app.Activity
import androidx.viewbinding.ViewBinding

class HttpRequesetDataset(
    latitude: Double,
    longitude: Double,
    activity: Activity?,
    binding: ViewBinding?
) {
    private var _x: Double = 0.0
    private var _y: Double = 0.0
    private var _activity: Activity? = null
    private var _binding: ViewBinding? = null
    private var _cityname: String? = null

    init {
        _x = latitude
        _y = longitude
        _activity = activity
        _binding = binding
    }

    public fun getX(): Double {
        return _x
    }

    public fun getY(): Double {
        return _y
    }

    public fun getActivity(): Activity? {
        return _activity
    }

    public fun getBinding(): ViewBinding? {
        return _binding
    }

    public fun getCityName(): String? {
        return _cityname
    }

    public fun setX(x: Double) {
        _x = x
    }

    public fun setY(y: Double) {
        _y = y
    }

    public fun setActivity(activity: Activity?) {
        _activity = activity
    }

    public fun setViewBinding(binding: ViewBinding?) {
        _binding = binding
    }

    public fun setCityName(cityname: String) {
        _cityname = cityname
    }
}