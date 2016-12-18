package com.heating.view

import android.content.Context
import android.graphics.*
import android.support.v4.view.MotionEventCompat
import android.view.MotionEvent
import android.view.View
import com.heating.R

class TemperatureLimitView(myContext: Context): View(myContext) {
    val _paint: Paint = Paint()
    val _scale: FloatArray = floatArrayOf(20F, 40F, 60F, 80F, 100F, 120F, 140F, 160F, 180F,
            200F, 220F, 240F, 260F)
    val _temperatureScale:IntArray = intArrayOf(30, 29, 28, 27, 26, 25, 24, 23, 22 , 21, 20, 19, 18)
    var _currentPositionIndex: Int = 0
    var _previousPositionIndex: Int = 0
    var _callbacks: MainView? = null

    fun setCallbacks(view: MainView){
        _callbacks = view
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawImage(canvas)
        drawTemperatureText(canvas)
    }

    private fun drawImage(canvas: Canvas?){
        val b = BitmapFactory.decodeResource(resources, R.drawable.temperature_limit)
        _paint.color = Color.RED
        canvas?.drawBitmap(b, 0F, _scale[_currentPositionIndex], _paint)
    }

    private fun drawTemperatureText(canvas: Canvas?){
        _paint.color = Color.rgb(82, 100, 110)
        _paint.style = Paint.Style.FILL
        _paint.textSize = 25F
        val temperature = _temperatureScale[_currentPositionIndex].toString() + " \u2103"
        canvas?.drawText(temperature, 15F, 30F+_scale[_currentPositionIndex], _paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action: Int = MotionEventCompat.getActionMasked(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                val pointerIndex: Int = MotionEventCompat.getActionIndex(event)
                val y: Float = event?.getY(pointerIndex) ?: 0F
                tryMoveScale(y)
                this.invalidate()
            }

            MotionEvent.ACTION_UP -> {
                if(_previousPositionIndex != _currentPositionIndex){
                    _previousPositionIndex = _currentPositionIndex
                    val temperature = _temperatureScale[_currentPositionIndex]
                    _callbacks?.onTemperatureChangeLimit(temperature)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val pointerIndex: Int = MotionEventCompat.getActionIndex(event)
                val y: Float = event?.getY(pointerIndex) ?: 0F
                tryMoveScale(y)
                this.invalidate()
            }
        }
        return true
    }

    private fun tryMoveScale(position:Float){
        for (i in 0.._scale.size - 1) {
            if (position>=_scale[i] && (i == _scale.size - 1 ||  position< _scale[i+1])){
                _currentPositionIndex = i
            }
        }
    }
}