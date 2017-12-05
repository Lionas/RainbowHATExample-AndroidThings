package jp.lionas.androidthings.rainbowhat

import android.view.KeyEvent
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

        buttonA = RainbowHat.createButtonAInputDriver(EVENT_BUTTON_A)
        buttonB = RainbowHat.createButtonBInputDriver(EVENT_BUTTON_B)
        buttonC = RainbowHat.createButtonCInputDriver(EVENT_BUTTON_C)

        buttonA?.register()
        buttonB?.register()
        buttonC?.register()

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
        val EVENT_BUTTON_A = KeyEvent.KEYCODE_X
        val EVENT_BUTTON_B = KeyEvent.KEYCODE_Y
        val EVENT_BUTTON_C = KeyEvent.KEYCODE_Z
    }

}