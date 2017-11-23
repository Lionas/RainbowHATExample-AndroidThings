package jp.lionas.androidthings.rainbowhat

import android.app.Activity

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

    // --- Life Cycle
    override fun onResume() {

        super.onResume()

        led.init()
        button.init()
        segment.init()
        buzzer.init()
        ledStrip.init()

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
                segment.display(Buzzer.TYPE.HIGH)
                buzzer.play(Buzzer.TYPE.HIGH)
                return true
            }

            RainbowHatButton.EVENT_BUTTON_B -> {
                led.onB()
                segment.display(Buzzer.TYPE.MIDDLE)
                buzzer.play(Buzzer.TYPE.MIDDLE)
                ledStrip.on()
                return true
            }

            RainbowHatButton. EVENT_BUTTON_C -> {
                led.onC()
                segment.display(Buzzer.TYPE.LOW)
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
                led.off()
                buzzer.stop()
                return true
            }

            RainbowHatButton.EVENT_BUTTON_B -> {
                led.off()
                buzzer.stop()
                return true
            }

            RainbowHatButton.EVENT_BUTTON_C -> {
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

    }

}
