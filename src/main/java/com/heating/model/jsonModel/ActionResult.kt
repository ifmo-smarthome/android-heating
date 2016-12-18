package com.heating.model.jsonModel

class ActionResult   {
    var success:Boolean = false
    var error: String? = null
    var result: HeatingActionResult? = null
}

class HeatingActionResult{
    var temperature: Int = 0
}