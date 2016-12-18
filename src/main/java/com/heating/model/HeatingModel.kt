package com.heating.model

import com.heating.presenters.MainPresenter
import com.heating.model.jsonModel.*
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class HeatingModel(serverUrl:String){
    val gson = Gson()
    val url  = serverUrl
    var callback: MainPresenter? = null

    fun startUpdateTemperatureThread(){
        val scheduleTaskExecutor = Executors.newScheduledThreadPool(5)
        scheduleTaskExecutor.scheduleAtFixedRate({ getCurrentTemperature() }, 0, 5, TimeUnit.SECONDS)
    }

    fun getCurrentTemperature(){
        var action = HeatingAction()
        action.action = "getTemperature"
        var content = gson.toJson(action)
        sendRequest(content, {onTemperatureActionSuccess(it)})
    }

    fun changeBatteryMode(mode: String){
        var parameters = HeatingActionParameters()
        parameters.mode = mode
        var action = HeatingAction()
        action.action = "changeBatteryMode"
        action.parameters = parameters
        var content = gson.toJson(action)
        sendRequest(content, {onChangeModeSuccess(it)})
    }

    fun changeTemperatureLimit(limit:Int){
        var parameters = HeatingActionParameters()
        parameters.limit = limit
        var action = HeatingAction()
        action.action = "changeTemperatureLimit"
        action.parameters = parameters
        var content = gson.toJson(action)
        sendRequest(content, {onChangeTemperatureLimitSuccess(it)})
    }

    private fun onTemperatureActionSuccess(result:String){
        var data = gson.fromJson<ActionResult>(result)
        var temperature = data.result?.temperature?:0
        callback?.updateTemperatureValue(temperature)
    }

    private fun onChangeModeSuccess(result: String){

    }

    private fun onChangeTemperatureLimitSuccess(result: String){

    }

    private fun sendRequest(content:String,
                    onSuccess: (result:String)->Unit){
        FuelManager.instance.baseHeaders = mapOf("Content-Type" to "application/json")
        Fuel.post(url).body(content).responseString{request, response, result->
            when(result){
                is Result.Failure->{
                }
                is Result.Success->{
                    onSuccess(result.get())
                }
            }
        }
    }
}