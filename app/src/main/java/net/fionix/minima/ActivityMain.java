package net.fionix.minima;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Arrays;

public class ActivityMain extends AppCompatActivity {

    public static Context contextMain;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigation_view:
                    manager.beginTransaction().replace(R.id.content, new ActivityCourse()).commit();
                    return true;
                case R.id.navigation_list:
                    manager.beginTransaction().replace(R.id.content, new ActivityTimetable()).commit();
                    return true;
                case R.id.navigation_setting:
                    manager.beginTransaction().replace(R.id.content, new ActivitySettings()).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // app shortcut
        ShortcutManager shortcutManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                shortcutManager = getSystemService(ShortcutManager.class);

                ShortcutInfo shortcut_simsweb = new ShortcutInfo.Builder(this, "id1")
                        .setShortLabel("simsweb")
                        .setLongLabel("simsweb")
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_link))
                        .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("http://simsweb.uitm.edu.my/SPORTAL_APP/SPORTAL_LOGIN/index.htm")))
                        .build();

                ShortcutInfo shortcut_icress = new ShortcutInfo.Builder(this, "id2")
                        .setShortLabel("iCress")
                        .setLongLabel("iCress")
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_link))
                        .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("http://icress.uitm.edu.my/")))
                        .build();

                shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut_simsweb, shortcut_icress));
            }
        }

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content, new ActivityCourse()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // check for opening from widget
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String extras = bundle.getString("Source");
            if (extras != null) {
                if (extras.contains("Widget")) {
                    navigation.setSelectedItemId(R.id.navigation_list);
                }
            }
        }

        contextMain = this.getBaseContext();
    }
}
