package com.heating.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.heating.R

class TemperatureView(myContext: Context): View(myContext)  {

    val _paint: Paint = Paint()
    val _scale: FloatArray = floatArrayOf(20F, 40F, 60F, 80F, 100F, 120F, 140F, 160F, 180F,
            200F, 220F, 240F, 260F)
    val _temperatureScale:IntArray = intArrayOf(30, 29, 28, 27, 26, 25, 24, 23, 22 , 21, 20, 19, 18)

    var _temperature: Int = 0

    fun setTemperature(temperature:Int){
        _temperature = temperature
        this.invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawImage(canvas)
        drawTemperatureText(canvas)
    }

    private fun drawImage(canvas: Canvas?){
        val b = BitmapFactory.decodeResource(resources, R.drawable.current_temperature)
        _paint.color = Color.RED
        val shift = getShift()
        canvas?.drawBitmap(b, 0F, shift, _paint)
    }

    private fun drawTemperatureText(canvas: Canvas?){
        _paint.color = Color.WHITE
        _paint.style = Paint.Style.FILL
        _paint.textSize = 25F
        val shift = getShift()+ 30F
        canvas?.drawText(_temperature.toString() + " \u2103", 30F, shift, _paint)
    }

    private fun getShift():Float{
        val index = _temperatureScale.indexOf(_temperature)
        var shift = 0F
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