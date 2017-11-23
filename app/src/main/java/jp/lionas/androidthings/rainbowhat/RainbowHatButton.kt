package jp.lionas.androidthings.rainbowhat

import android.view.KeyEvent
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.button.ButtonInputDriver
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

/**
 * Button管理クラス
 * Created by lionas on 2017/11/23.
 */
class RainbowHatButton {

    private var buttonA: ButtonInputDriver? = null
    private var buttonB: ButtonInputDriver? = null
    private var buttonC: ButtonInputDriver? = null

    fun init() {

        buttonA = initButton(RainbowHat.BUTTON_A, EVENT_BUTTON_A)
        buttonB = initButton(RainbowHat.BUTTON_B, EVENT_BUTTON_B)
        buttonC = initButton(RainbowHat.BUTTON_C, EVENT_BUTTON_C)

    }

    private fun initButton(portName: String, keycode: Int): ButtonInputDriver? {

        val buttonInputDriver : ButtonInputDriver? = ButtonInputDriver(
                portName,
                Button.LogicState.PRESSED_WHEN_LOW,
                keycode)
        buttonInputDriver?.register()

        return buttonInputDriver

    }

    fun close() {

        closeButton(buttonA)
        closeButton(buttonB)
        closeButton(buttonC)

    }

    private fun closeButton(button: ButtonInputDriver?) {

        button?.unregister()
        button?.close()

    }

    companion object {
        val EVENT_BUTTON_A = KeyEvent.KEYCODE_0
        val EVENT_BUTTON_B = KeyEvent.KEYCODE_1
        val EVENT_BUTTON_C = KeyEvent.KEYCODE_2
    }

}