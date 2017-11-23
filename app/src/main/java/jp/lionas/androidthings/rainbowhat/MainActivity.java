package jp.lionas.androidthings.rainbowhat;

import android.app.Activity;

import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import android.graphics.Color;
import com.google.android.things.contrib.driver.apa102.Apa102;
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay;
import com.google.android.things.contrib.driver.ht16k33.Ht16k33;
import com.google.android.things.contrib.driver.pwmspeaker.Speaker;
import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.pio.Gpio;
import android.util.Log;
import android.view.KeyEvent;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final String TAG = "RainbowHATExample";

    private static final int EVENT_BUTTON_A = KeyEvent.KEYCODE_0;
    private static final int EVENT_BUTTON_B = KeyEvent.KEYCODE_1;
    private static final int EVENT_BUTTON_C = KeyEvent.KEYCODE_2;

    enum BUZZER {
        LOW(400),
        MIDDLE(800),
        HIGH(1600)
        ;

        private final int freq;

        BUZZER(final int freq) {
            this.freq = freq;
        }

        public int getFreq() {
            return this.freq;
        }

        public String getDisplayFreq() {
            return String.format(Locale.ENGLISH,"%04d", this.freq);
        }
    }

    private Gpio mLedA;
    private Gpio mLedB;
    private Gpio mLedC;
    private ButtonInputDriver mButtonA;
    private ButtonInputDriver mButtonB;
    private ButtonInputDriver mButtonC;
    private Apa102 ledStrip;
    private AlphanumericDisplay segment;
    private Speaker buzzer;
    private int[] rainbow = new int[7];

    // --- Life Cycle
    @Override
    protected void onResume() {

        super.onResume();

        initLEDs();
        initButtons();
        initAlphanumericDisplay();
        initBuzzer();
        init7LED();

    }

    @Override
    protected void onPause(){
        closeAll();
        super.onPause();
    }

    // --- Key Event
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case EVENT_BUTTON_A:
                setLed(mLedA, true);
                setAlphanumericDisplay(BUZZER.HIGH.getDisplayFreq());
                playBuzzer(BUZZER.HIGH.getFreq());
                return true;

            case EVENT_BUTTON_B:
                setLed(mLedB, true);
                setAlphanumericDisplay(BUZZER.MIDDLE.getDisplayFreq());
                playBuzzer(BUZZER.MIDDLE.getFreq());
                powerOn7LED();
                return true;

            case EVENT_BUTTON_C:
                setLed(mLedC, true);
                setAlphanumericDisplay(BUZZER.LOW.getDisplayFreq());
                playBuzzer(BUZZER.LOW.getFreq());
                powerOff7LED();
                return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case EVENT_BUTTON_A:
                setLed(mLedA, false);
                stopBuzzer();
                return true;

            case EVENT_BUTTON_B:
                setLed(mLedB, false);
                stopBuzzer();
                return true;

            case EVENT_BUTTON_C:
                setLed(mLedC, false);
                stopBuzzer();
                return true;
        }

        return super.onKeyUp(keyCode, event);

    }

    // --- LED
    private void initLEDs() {

        mLedA = initLED(mLedA, RainbowHat.LED_RED);
        mLedB = initLED(mLedB, RainbowHat.LED_GREEN);
        mLedC = initLED(mLedC, RainbowHat.LED_BLUE);

    }

    private Gpio initLED(Gpio led, String color) {

        try {
            if (led == null) {
                led = RainbowHat.openLed(color);
            }
            led.setValue(false);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        return led;

    }

    private void closeLEDs() {

        closeLED(mLedA);
        closeLED(mLedB);
        closeLED(mLedC);

    }

    private void closeLED(Gpio led) {

        if (led != null) {
            try {
                led.setValue(false);
                led.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            } finally{
                led = null;
            }
        }

    }

    private void setLed(Gpio led, boolean value) {

        try {
            led.setValue(value);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

    }

    // --- Button
    private void initButtons() {

        mButtonA = initButton(RainbowHat.BUTTON_A, EVENT_BUTTON_A);
        mButtonB = initButton(RainbowHat.BUTTON_B, EVENT_BUTTON_B);
        mButtonC = initButton(RainbowHat.BUTTON_C, EVENT_BUTTON_C);

    }

    private ButtonInputDriver initButton(String portName, int keycode) {

        ButtonInputDriver buttonInputDriver = null;

        try {
            buttonInputDriver = new ButtonInputDriver(
                    portName,
                    Button.LogicState.PRESSED_WHEN_LOW,
                    keycode);
            buttonInputDriver.register();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        return buttonInputDriver;

    }

    private void closeButtons() {

        closeButton(mButtonA);
        closeButton(mButtonB);
        closeButton(mButtonC);

    }

    private void closeButton(ButtonInputDriver button) {

        if (button != null) {
            button.unregister();
            try {
                button.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            } finally{
                button = null;
            }
        }

    }

    // --- RainbowLED
    private void init7LED() {

        if (ledStrip == null) {
            try {
                ledStrip = RainbowHat.openLedStrip();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return;
            }

            ledStrip.setBrightness(1);
            for (int i = 0; i < rainbow.length; i++) {
                rainbow[i] = Color.HSVToColor(255, new float[]{i * 360.f / rainbow.length, 1.0f, 1.0f});
            }
        }

    }

    private void close7LED() {

        if(ledStrip != null) {
            try {
                ledStrip.close();
                ledStrip = null;
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }

    }

    private void powerOn7LED() {

        if (ledStrip != null) {
            try {
                ledStrip.setBrightness(1);
                ledStrip.write(rainbow);
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
        }

    }

    private void powerOff7LED() {

        if(ledStrip != null) {
            try {
                ledStrip.setBrightness(0);
                ledStrip.write(rainbow);
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
        }

    }

    // --- Seven Segment
    private void initAlphanumericDisplay() {

        try {
            if (segment == null) {
                segment = RainbowHat.openDisplay();
            }
            segment.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX);
            segment.setEnabled(true);
            segment.display("INIT");
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }

    }

    private void setAlphanumericDisplay(String text) {

        if (segment != null) {
            try {
                segment.display(text);
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
        }

    }

    private void closeAlphanumericDisplay() {

        if (segment != null) {
            try {
                segment.clear();
                segment.close();
                segment = null;
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
        }

    }

    // --- Buzzer
    private void initBuzzer() {

        if (buzzer == null) {
            try {
                buzzer = RainbowHat.openPiezo();
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
        }

    }

    private void playBuzzer(int freq) {

        if (buzzer != null) {
            try {
                buzzer.play(freq);
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
        }
    }

    private void stopBuzzer() {

        if (buzzer != null) {
            try {
                buzzer.stop();
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
        }
    }

    private void closeBuzzer() {

        if (buzzer != null) {
            try {
                stopBuzzer();
                buzzer.close();
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            } finally {
                buzzer = null;
            }
        }
    }

    private void closeAll() {
        closeLEDs();
        closeButtons();
        close7LED();
        closeAlphanumericDisplay();
        closeBuzzer();
    }

}
