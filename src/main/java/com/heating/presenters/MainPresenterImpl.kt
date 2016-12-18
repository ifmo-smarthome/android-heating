package com.heating.presenters

import com.heating.model.HeatingModel
import com.heating.view.MainView

class MainPresenterImpl(val heatingModel:HeatingModel, val mainView:MainView): MainPresenter{
    init{
        heatingModel.callback = this
        heatingModel.startUpdateTemperatureThread()
    }

    override fun updateTemperatureValue(temperature:Int) {
        mainView.updateTemperatureValue(temperature)
    }

    override fun changeBatteryMode(mode:String) {
        heatingModel.changeBatteryMode(mode)
    }

    override fun changeTemperatureLimit(temperatureLimit: Int) {
        heatingModel.changeTemperatureLimit(temperatureLimit)
    }
}