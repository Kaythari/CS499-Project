package com.example.kaythari.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kaythari on 4/27/2017.
 */

public class TouchToNext extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //brightness at max
        WindowManager.LayoutParams layout2 = getWindow().getAttributes();
        layout2.screenBrightness = 1F;
        getWindow().setAttributes(layout2);

        //fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen1);

        //Custom Font
        Typewriter t = (Typewriter)findViewById(R.id.typewriter);
        Typeface custom = Typeface.createFromAsset(getAssets(), "fonts/Buried Bones.ttf");
        t.setTypeface(custom);
        t.setCharacterDelay(150);
        t.animateText("Mortal! You think you can rise on your own?");

        //Testing
        LinearLayout layout = (LinearLayout) findViewById(R.id.splash1);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(TouchToNext.this, TouchToNext2.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
    }

    //disable back button
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "Don't you dare leave!", Toast.LENGTH_SHORT).show();
    }
}
