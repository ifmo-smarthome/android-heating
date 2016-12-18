package com.heating.presenters

interface MainPresenter{
    fun updateTemperatureValue(temperature:Int)
    fun changeBatteryMode(mode: String)
    fun changeTemperatureLimit(temperatureLimit: Int)
}