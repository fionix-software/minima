package net.fionix.minima;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // get variables
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean first = sharedPreferences.getBoolean("first", true);

        // splash screen
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);

                    // redirect
                    if (first) {
                        Intent intent = new Intent(getApplicationContext(), ActivityIntro.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                        startActivity(intent);
                    }

                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splashThread.start();
    }
}
