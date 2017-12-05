package jp.lionas.androidthings.rainbowhat

import android.hardware.SensorManager
import com.google.android.things.contrib.driver.bmx280.Bmx280SensorDriver
import com.google.android.things.pio.PeripheralManagerService
import android.content.ContentValues.TAG
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log

/**
 * センサー管理クラス
 * Created by lionas on 2017/11/23.
 */
class RainbowHatSensor {

    private var sensorManager: SensorManager? = null
    private var managerService = PeripheralManagerService()
    private var sensorDriver: Bmx280SensorDriver? = null
    private var segment : Segment? = null
    private var displayCount = 0

    fun init(sensorManager: SensorManager, segment: Segment) {
        this.sensorManager = sensorManager
        sensorDriver = Bmx280SensorDriver((managerService.i2cBusList[0]))
        this.segment = segment
        sensorDriver?.registerPressureSensor()
        sensorDriver?.registerTemperatureSensor()
        sensorDriver?.registerHumiditySensor()
    }

    fun startSensor() {

        val lists : List<Sensor>? = sensorManager?.getDynamicSensorList(Sensor.TYPE_ALL)
        for (sensor in lists!!) {
            sensorManager?.registerListener(onEvent, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }

    fun stopSensor() {

        val lists : List<Sensor>? = sensorManager?.getDynamicSensorList(Sensor.TYPE_ALL)
        for (sensor in lists!!) {
            sensorManager?.unregisterListener(onEvent)
        }

    }

    fun close() {

        sensorDriver?.unregisterTemperatureSensor()
        sensorDriver?.unregisterPressureSensor()
        sensorDriver?.unregisterHumiditySensor()
        sensorDriver?.close()
        sensorManager?.unregisterListener(onEvent)

    }

    private var onEvent: SensorEventListener = object : SensorEventListener {

        override fun onSensorChanged(sensorEvent: SensorEvent) {

            when (sensorEvent.sensor.type) {

                Sensor.TYPE_AMBIENT_TEMPERATURE -> {
//                    Log.d(TAG, "TEMPERATURE=" + sensorEvent.values[0])
                    if (displayCount < 100) {
                        segment?.display("%4f.1".format(sensorEvent.values[0]))
                    }
                }

                Sensor.TYPE_PRESSURE -> {
//                    Log.d(TAG, "PRESSURE=" + sensorEvent.values[0])
                    if (displayCount in 100..199) {
                        segment?.display("%4f.0".format(sensorEvent.values[0]))
                    }
                }

                Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    Log.d(TAG, "HUMIDITY=" + sensorEvent.values[0])
                    if (displayCount in 200..299) {
                        segment?.display("%4f.1".format(sensorEvent.values[0]))
                    }
                }

            }

            if (++displayCount >= 300) {
                displayCount = 0
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, i: Int) {

        }
    }

}