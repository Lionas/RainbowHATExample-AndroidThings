package jp.lionas.androidthings.rainbowhat

import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

/**
 * ブザー管理クラス
 * Created by lionas on 2017/11/23.
 */
class Buzzer {

    private var speaker: Speaker? = null

    enum class TYPE constructor(val freq: Int) {

        LOW(100),
        MIDDLE(800),
        HIGH(1600);

        val displayFreq: String
            get() = "%04d".format(this.freq)

        val double: Double
            get() = this.freq.toDouble()

    }

    fun init() {

        if (speaker == null) {
            speaker = RainbowHat.openPiezo()
        }

    }

    fun play(type: TYPE) {
        speaker?.play(type.double)
    }

    fun stop() {
        speaker?.stop()
    }

    fun close() {

       speaker?.stop()
       speaker?.close()

    }

}