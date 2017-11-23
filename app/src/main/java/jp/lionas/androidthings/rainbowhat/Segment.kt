package jp.lionas.androidthings.rainbowhat

import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.ht16k33.Ht16k33
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

/**
 * 7SegmentLED管理クラス
 * Created by lionas on 2017/11/23.
 */
class Segment {

    var alphaDisplay: AlphanumericDisplay? = null

    fun init() {

        if (alphaDisplay == null) {
            alphaDisplay = RainbowHat.openDisplay()
        }

        alphaDisplay?.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
        alphaDisplay?.setEnabled(true)
        alphaDisplay?.display("INIT")

    }

    fun display(type: Buzzer.TYPE) {
        alphaDisplay?.display(type.displayFreq)
    }

    fun close() {

        alphaDisplay?.clear()
        alphaDisplay?.close()
        alphaDisplay = null

    }

}