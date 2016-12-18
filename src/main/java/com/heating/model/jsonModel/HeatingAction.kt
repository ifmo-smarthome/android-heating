package com.heating.model.jsonModel

class HeatingAction {
    var action:String? = null
    var parameters: HeatingActionParameters? = null
}

class HeatingActionParameters{
    var mode: String? = null
    var limit: Int? = null
}