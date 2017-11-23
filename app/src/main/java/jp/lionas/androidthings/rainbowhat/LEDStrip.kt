package jp.lionas.androidthings.rainbowhat

import android.graphics.Color
import com.google.android.things.contrib.driver.apa102.Apa102
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

/**
 * LEDStrip管理クラス
 * Created by lionas on 2017/11/23.
 */
class LEDStrip {

    private var ledStrip: Apa102? = null
    private val rainbow = IntArray(7)

    fun init() {

        if (ledStrip == null) {
            ledStrip = RainbowHat.openLedStrip()
        }

        ledStrip?.setBrightness(1)
        for (i in 0 until rainbow.size) {
            rainbow[i] = Color.HSVToColor(255, floatArrayOf(i * 360f / rainbow.size, 1.0f, 1.0f))
        }

    }

    fun close() {

        ledStrip?.close()
        ledStrip = null

    }

    fun on() {

        ledStrip?.setBrightness(1)
        ledStrip?.write(rainbow)

    }

    fun off() {

        ledStrip?.setBrightness(0)
        ledStrip?.write(rainbow)

    }

}