package com.heating.view

interface  MainView{
    fun updateTemperatureValue(newValue: Int)
    fun onTemperatureChangeLimit(temperatureLimit: Int)
}