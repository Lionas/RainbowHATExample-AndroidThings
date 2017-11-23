package jp.lionas.androidthings.rainbowhat

import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio

/**
 * LED管理クラス
 * Created by lionas on 2017/11/23.
 */
class LED {

    private var ledA: Gpio? = null
    private var ledB: Gpio? = null
    private var ledC: Gpio? = null

    fun init() {

        ledA = initLED(ledA, RainbowHat.LED_RED)
        ledB = initLED(ledB, RainbowHat.LED_GREEN)
        ledC = initLED(ledC, RainbowHat.LED_BLUE)

    }

    private fun initLED(ledGpio: Gpio?, color: String): Gpio? {

        var led : Gpio? = ledGpio

        if (led == null) {
            led = RainbowHat.openLed(color)
        }
        led?.value = false

        return led

    }

    fun onA() {
        setValue(true, false, false)
    }

    fun onB() {
        setValue(false, true, false)
    }

    fun onC() {
        setValue(false, false, true)
    }

    fun off() {
        setValue(false, false, false)
    }

    private fun setValue(a: Boolean, b: Boolean, c: Boolean) {
        ledA?.value = a
        ledB?.value = b
        ledC?.value = c
    }

    fun close() {

        closeLED(ledA)
        closeLED(ledB)
        closeLED(ledC)

    }

    private fun closeLED(ledGpio: Gpio?) {

        ledGpio?.value = false
        ledGpio?.close()

    }

}