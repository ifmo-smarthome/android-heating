package com.heating

import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewManager
import android.widget.*
import com.heating.model.*
import com.heating.presenters.*
import com.heating.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView

class MainActivity : AppCompatActivity(), MainView {
    lateinit var _presenter:MainPresenter
    val _thisActivity:MainView = this
    var _temperatureView: TemperatureView? = null
    var _termometerView: TermometerView? = null

    inline fun ViewManager.temperatureLimitView(theme: Int = 0, init: TemperatureLimitView.() -> Unit)
            = ankoView(::TemperatureLimitView, theme, init)

    inline fun ViewManager.temperatureView(theme: Int = 0, init: TemperatureView.() -> Unit)
            = ankoView(::TemperatureView, theme, init)

    inline fun ViewManager.termometerView(theme: Int = 0, init: TermometerView.() -> Unit)
            = ankoView(::TermometerView, theme, init)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createLayout()
        val url = resources.getString(R.string.server_url)
        var heatingModel = HeatingModel(url)
        _presenter = MainPresenterImpl(heatingModel,this)
    }

    private fun createLayout(){
        relativeLayout{
            backgroundColor = Color.rgb(245,245,245)

            relativeLayout{
                textView{
                    id = R.id.status_title
                    text="Target"
                }.lparams{
                    alignParentLeft()
                    leftMargin = dip(60)
                }

                textView{
                    id = R.id.status_title
                    text="Actual"
                }.lparams{
                    alignParentRight()
                    rightMargin = dip(60)

                }
            }.lparams(){
                topPadding = dip(10)
            }

            //термометер
            linearLayout{
                bottomPadding = dip(30)
                temperatureLimitView(){
                    setCallbacks(_thisActivity)
                }.lparams{
                    width = dip(70)
                    height = dip(280)
                }
                _termometerView = termometerView {
                }.lparams{
                    height = dip(280)
                    width = dip(100)
                }

                _temperatureView = temperatureView(){
                }.lparams{
                    width = dip(70)
                    height = dip(280)
                }
            }.lparams{
                centerHorizontally()
                topOf(R.id.status_title)
                android.R.attr.layout_weight
            }

            textView{
                id = R.id.status_title
                text="Heating Status"
                bottomPadding=dip(15)
            }.lparams{
                centerHorizontally()
                topOf(R.id.bottom_layout_buttons)
            }

            linearLayout {
                id=R.id.bottom_layout_buttons
                gravity=Gravity.BOTTOM
                orientation = LinearLayout.HORIZONTAL

                radioGroup {
                    id = R.id.button_group
                    orientation = LinearLayout.HORIZONTAL

                    setOnCheckedChangeListener { radioGroup, i ->onGroupCheckedChange(radioGroup, i)}
                    toggleButton{
                        id=R.id.button_on
                        configureToggleButton(this, "ON")
                    }
                    toggleButton{
                        id = R.id.button_off
                        configureToggleButton(this, "OFF")
                    }
                    toggleButton{
                        id = R.id.button_auto
                        configureToggleButton(this, "AUTO")
                    }
                }
            }.lparams{
                alignParentBottom()
                centerHorizontally()
            }

        }
    }

    private fun onGroupCheckedChange(radioGroup:RadioGroup, i: Int){
        for(j in 0..radioGroup.childCount-1){
            val view = radioGroup.getChildAt(j) as ToggleButton
            val isChecked = view.id == i
            view.isChecked = isChecked
            val backgroundColor = if(isChecked)  Color.rgb(1,186,242) else
                Color.rgb(186,203,211)
            view.background.setColorFilter(backgroundColor, PorterDuff.Mode.MULTIPLY)
        }
    }

    private fun onToggle(button:ToggleButton){
        button.isChecked = true
        (button.parent as _RadioGroup).check(button.id)
        _presenter.changeBatteryMode(button.text.toString())
    }

    private fun configureToggleButton(button:ToggleButton, text:String){
        button.textColor = Color.WHITE
        button.background.setColorFilter(Color.rgb(186,203,211), PorterDuff.Mode.MULTIPLY)
        button.text=text
        button.textOn=text
        button.textOff=text
        button.onClick{onToggle(button)}
    }

    override fun updateTemperatureValue(newValue: Int) {
        _temperatureView?.setTemperature(newValue)
        _termometerView?.setTemperature(newValue)
    }

    override fun onTemperatureChangeLimit(temperatureLimit: Int) {
        _presenter.changeTemperatureLimit(temperatureLimit)
    }

}
