package jp.lionas.androidthings.rainbowhat

import android.app.Activity
import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle

import android.view.KeyEvent

/**
 * 起動時に立ち上がる画面（制御）クラス
 */
class MainActivity : Activity() {

    private val led = LED()
    private val button = RainbowHatButton()
    private val ledStrip = LEDStrip()
    private val segment = Segment()
    private val buzzer = Buzzer()
    private val sensor = RainbowHatSensor()

    // --- Life Cycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager : SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor.init(manager, segment)
    }

    override fun onResume() {

        super.onResume()

        led.init()
        button.init()
        segment.init()
        buzzer.init()
        ledStrip.init()
        sensor.startSensor()

    }

    override fun onPause() {
        closeAll()
        super.onPause()
    }

    // --- Key Event
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        when (keyCode) {

            RainbowHatButton.EVENT_BUTTON_A -> {
                led.onA()
                sensor.stopSensor()
                segment.display(Buzzer.TYPE.HIGH.displayFreq)
                buzzer.play(Buzzer.TYPE.HIGH)
                ledStrip.off()
                return true
            }

            RainbowHatButton.EVENT_BUTTON_B -> {
                led.onB()
                sensor.stopSensor()
                segment.display(Buzzer.TYPE.MIDDLE.displayFreq)
                buzzer.play(Buzzer.TYPE.MIDDLE)
                ledStrip.on()
                return true
            }

            RainbowHatButton. EVENT_BUTTON_C -> {
                led.onC()
                sensor.stopSensor()
                segment.display(Buzzer.TYPE.LOW.displayFreq)
                buzzer.play(Buzzer.TYPE.LOW)
                ledStrip.off()
                return true
            }
        }

        return super.onKeyDown(keyCode, event)

    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {

        when (keyCode) {
            RainbowHatButton.EVENT_BUTTON_A -> {
                sensor.startSensor()
                led.off()
                buzzer.stop()
                return true
            }

            RainbowHatButton.EVENT_BUTTON_B -> {
                sensor.startSensor()
                led.off()
                buzzer.stop()
                return true
            }

            RainbowHatButton.EVENT_BUTTON_C -> {
                sensor.startSensor()
                led.off()
                buzzer.stop()
                return true
            }
        }

        return super.onKeyUp(keyCode, event)

    }

    private fun closeAll() {

        led.close()
        button.close()
        ledStrip.close()
        segment.close()
        buzzer.close()
        sensor.close()

    }

}
