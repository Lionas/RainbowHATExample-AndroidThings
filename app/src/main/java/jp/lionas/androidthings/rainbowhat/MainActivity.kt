package jp.lionas.androidthings.rainbowhat

import android.app.Activity
import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log

import android.view.KeyEvent
import android.content.ContentValues.TAG
import com.google.android.things.pio.PeripheralManagerService



/**
 * 起動時に立ち上がる画面（制御）クラス
 */
class MainActivity : Activity() {

//    private val led = LED()
//    private val button = RainbowHatButton()
//    private val ledStrip = LEDStrip()
//    private val segment = Segment()
    private val buzzer = Buzzer()
//    private val sensor = RainbowHatSensor()
    private val mpr121 = Mpr121Helper()

    // --- Life Cycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val manager : SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        sensor.init(manager, segment)


        mpr121.init("I2C1", object : Mpr121Helper.OnTouchListener {
            override fun onSensorTouched(id: Int) {
                Log.d("MainActivity", "id = $id")
                buzzer.play(Buzzer.TYPE.HIGH)
            }

            override fun onSensorReleased(id: Int) {
                buzzer.stop()
            }
        })

    }

    override fun onResume() {

        super.onResume()

//        led.init()
//        button.init()
//        segment.init()
        buzzer.init()
//        ledStrip.init()
//        sensor.startSensor()

    }

    override fun onPause() {
        closeAll()
        super.onPause()
    }

    // --- Key Event
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        when (keyCode) {

            RainbowHatButton.EVENT_BUTTON_A -> {
//                led.onA()
//                sensor.stopSensor()
//                segment.display(Buzzer.TYPE.HIGH.displayFreq)
//                buzzer.play(Buzzer.TYPE.HIGH)
//                ledStrip.off()
                return true
            }

            RainbowHatButton.EVENT_BUTTON_B -> {
//                led.onB()
//                sensor.stopSensor()
//                segment.display(Buzzer.TYPE.MIDDLE.displayFreq)
//                buzzer.play(Buzzer.TYPE.MIDDLE)
//                ledStrip.on()
                return true
            }

            RainbowHatButton. EVENT_BUTTON_C -> {
//                led.onC()
//                sensor.stopSensor()
//                segment.display(Buzzer.TYPE.LOW.displayFreq)
//                buzzer.play(Buzzer.TYPE.LOW)
//                ledStrip.off()
                return true
            }
        }

        mpr121.onKeyDown(keyCode)

        return super.onKeyDown(keyCode, event)

    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {

        when (keyCode) {
            RainbowHatButton.EVENT_BUTTON_A -> {
//                sensor.startSensor()
//                led.off()
//                buzzer.stop()
                return true
            }

            RainbowHatButton.EVENT_BUTTON_B -> {
//                sensor.startSensor()
//                led.off()
//                buzzer.stop()
                return true
            }

            RainbowHatButton.EVENT_BUTTON_C -> {
//                sensor.startSensor()
//                led.off()
//                buzzer.stop()
                return true
            }
        }

        mpr121.onKeyUp(keyCode)

        return super.onKeyUp(keyCode, event)

    }

    private fun closeAll() {

//        led.close()
//        button.close()
//        ledStrip.close()
//        segment.close()
        buzzer.close()
//        sensor.close()
        mpr121.close()

    }

}
