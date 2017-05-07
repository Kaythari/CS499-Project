package com.example.kaythari.myfirstapp;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

/**
 * Created by Kaythari on 5/1/2017.
 */

public class Typewriter extends android.support.v7.widget.AppCompatTextView {

    private CharSequence text;
    private int i;
    private long delay = 500; //Default 500ms delay


    public Typewriter(Context context) {
        super(context);
    }

    public Typewriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler handler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(text.subSequence(0, i++));
            if(i <= text.length()) {
                handler.postDelayed(characterAdder, delay);
            }
        }
    };

    public void animateText(CharSequence text) {
        this.text = text;
        i = 0;

        setText("");
        handler.removeCallbacks(characterAdder);
        handler.postDelayed(characterAdder, delay);
    }

    public void setCharacterDelay(long millis) {
        delay = millis;
    }
}