package com.heating.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.heating.R

class TermometerView (myContext: Context): View(myContext)  {
    val _scale: IntArray = intArrayOf(50, 70, 90, 110, 130, 150, 170, 190, 210, 230, 250, 270, 290)
    val _temperatureScale:IntArray = intArrayOf(30, 29, 28, 27, 26, 25, 24, 23, 22 , 21, 20, 19, 18)
    var _temperature: Int = 0

    fun setTemperature(temperature: Int){
        _temperature = temperature
        this.invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawImage(canvas)
    }

    private fun drawImage(canvas: Canvas?){
        val res = resources
        val termBitmap = BitmapFactory.decodeResource(res, R.drawable.termometer2)
        val temperatureBitmap = BitmapFactory.decodeResource(res, R.drawable.fill_termometer)
        val termDrawable = BitmapDrawable(res, termBitmap)
        val temperatureDrawable = BitmapDrawable(res, temperatureBitmap)
        val shift = getShift()


        termDrawable.bounds = canvas?.clipBounds
        temperatureDrawable.setBounds(50, shift, 100, 300)
        termDrawable.draw(canvas)
        temperatureDrawable.draw(canvas)
    }

    private fun getShift():Int{
        val index = _temperatureScale.indexOf(_temperature)
        var shift = 0
        if(index == -1 && _temperature>_temperatureScale[0]){
            shift = _scale[0]
        }else if(index == -1 && _temperature<_temperatureScale[_scale.size-1]) {
            shift = _scale[_scale.size-1]
        }
        else{
            shift = _scale[index]
        }
        return shift
    }
}