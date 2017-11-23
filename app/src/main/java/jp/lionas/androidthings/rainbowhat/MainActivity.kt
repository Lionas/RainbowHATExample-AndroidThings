package jp.lionas.androidthings.rainbowhat

import android.app.Activity

import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import android.graphics.Color
import com.google.android.things.contrib.driver.apa102.Apa102
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.ht16k33.Ht16k33
import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.button.ButtonInputDriver
import com.google.android.things.pio.Gpio
import android.view.KeyEvent

class MainActivity : Activity() {

    private var mLedA: Gpio? = null
    private var mLedB: Gpio? = null
    private var mLedC: Gpio? = null
    private var mButtonA: ButtonInputDriver? = null
    private var mButtonB: ButtonInputDriver? = null
    private var mButtonC: ButtonInputDriver? = null
    private var ledStrip: Apa102? = null
    private var segment: AlphanumericDisplay? = null
    private var buzzer: Speaker? = null
    private val rainbow = IntArray(7)

    internal enum class BUZZER constructor(val freq: Int) {
        LOW(100),
        MIDDLE(800),
        HIGH(1600);

        val displayFreq: String
            get() = "%04d".format(this.freq)
    }

    // --- Life Cycle
    override fun onResume() {

        super.onResume()

        initLEDs()
        initButtons()
        initAlphanumericDisplay()
        initBuzzer()
        init7LED()

    }

    override fun onPause() {
        closeAll()
        super.onPause()
    }

    // --- Key Event
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        when (keyCode) {
            EVENT_BUTTON_A -> {
                mLedA!!.value = true
                segment?.display(BUZZER.HIGH.displayFreq)
                buzzer?.play(BUZZER.HIGH.freq.toDouble())
                return true
            }

            EVENT_BUTTON_B -> {
                mLedB!!.value = true
                segment?.display(BUZZER.MIDDLE.displayFreq)
                buzzer?.play(BUZZER.MIDDLE.freq.toDouble())
                powerOn7LED()
                return true
            }

            EVENT_BUTTON_C -> {
                mLedC!!.value = true
                segment?.display(BUZZER.LOW.displayFreq)
                buzzer?.play(BUZZER.LOW.freq.toDouble())
                powerOff7LED()
                return true
            }
        }

        return super.onKeyDown(keyCode, event)

    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {

        when (keyCode) {
            EVENT_BUTTON_A -> {
                mLedA?.value = false
                buzzer?.stop()
                return true
            }

            EVENT_BUTTON_B -> {
                mLedB?.value = false
                buzzer?.stop()
                return true
            }

            EVENT_BUTTON_C -> {
                mLedC?.value = false
                buzzer?.stop()
                return true
            }
        }

        return super.onKeyUp(keyCode, event)

    }

    // --- LED
    private fun initLEDs() {

        mLedA = initLED(mLedA, RainbowHat.LED_RED)
        mLedB = initLED(mLedB, RainbowHat.LED_GREEN)
        mLedC = initLED(mLedC, RainbowHat.LED_BLUE)

    }

    private fun initLED(ledGpio: Gpio?, color: String): Gpio? {

        var led : Gpio? = ledGpio

        if (led == null) {
            led = RainbowHat.openLed(color)
        }
        led!!.value = false

        return led

    }

    private fun closeLEDs() {

        closeLED(mLedA)
        closeLED(mLedB)
        closeLED(mLedC)

    }

    private fun closeLED(ledGpio: Gpio?) {

        ledGpio?.value = false
        ledGpio?.close()

    }

    // --- Button
    private fun initButtons() {

        mButtonA = initButton(RainbowHat.BUTTON_A, EVENT_BUTTON_A)
        mButtonB = initButton(RainbowHat.BUTTON_B, EVENT_BUTTON_B)
        mButtonC = initButton(RainbowHat.BUTTON_C, EVENT_BUTTON_C)

    }

    private fun initButton(portName: String, keycode: Int): ButtonInputDriver? {

       val buttonInputDriver : ButtonInputDriver? = ButtonInputDriver(
                portName,
                Button.LogicState.PRESSED_WHEN_LOW,
                keycode)
        buttonInputDriver?.register()

        return buttonInputDriver

    }

    private fun closeButtons() {

        closeButton(mButtonA)
        closeButton(mButtonB)
        closeButton(mButtonC)

    }

    private fun closeButton(button: ButtonInputDriver?) {

        button?.unregister()
        button?.close()

    }

    // --- RainbowLED
    private fun init7LED() {

        if (ledStrip == null) {
            ledStrip = RainbowHat.openLedStrip()
        }

        ledStrip?.setBrightness(1)
        for (i in 0 until rainbow.size) {
            rainbow[i] = Color.HSVToColor(255, floatArrayOf(i * 360f / rainbow.size, 1.0f, 1.0f))
        }

    }

    private fun close7LED() {

        ledStrip?.close()
        ledStrip = null

    }

    private fun powerOn7LED() {

        ledStrip?.setBrightness(1)
        ledStrip?.write(rainbow)

    }

    private fun powerOff7LED() {

        ledStrip?.setBrightness(0)
        ledStrip?.write(rainbow)

    }

    // --- Seven Segment
    private fun initAlphanumericDisplay() {

        if (segment == null) {
            segment = RainbowHat.openDisplay()
        }

        segment?.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
        segment?.setEnabled(true)
        segment?.display("INIT")

    }

    private fun closeAlphanumericDisplay() {

        segment?.clear()
        segment?.close()
        segment = null

    }

    // --- Buzzer
    private fun initBuzzer() {

        if (buzzer == null) {
            buzzer = RainbowHat.openPiezo()
        }

    }

    private fun closeBuzzer() {

        buzzer?.stop()
        buzzer?.close()

    }

    private fun closeAll() {
        closeLEDs()
        closeButtons()
        close7LED()
        closeAlphanumericDisplay()
        closeBuzzer()
    }

    companion object {
        private val EVENT_BUTTON_A = KeyEvent.KEYCODE_0
        private val EVENT_BUTTON_B = KeyEvent.KEYCODE_1
        private val EVENT_BUTTON_C = KeyEvent.KEYCODE_2
    }

}
