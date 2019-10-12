package net.fionix.minima;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityIntro extends AppCompatActivity {

    int slideMax = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final ImageView iconImage = findViewById(R.id.imageView1);
        final Button nextBtn = findViewById(R.id.button1);
        final TextView introTxt = findViewById(R.id.textView1);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideMax--;
                if (slideMax == 4) {
                    iconImage.setImageResource(R.drawable.ic_github);
                    introTxt.setText(R.string.intro2);
                } else if (slideMax == 3) {
                    iconImage.setImageResource(R.drawable.ic_code_branch);
                    introTxt.setText(R.string.intro3);
                } else if (slideMax == 2) {
                    iconImage.setImageResource(R.drawable.ic_handshake);
                    introTxt.setText(R.string.intro4);
                } else if (slideMax == 1) {
                    iconImage.setImageResource(R.drawable.ic_smile_wink);
                    introTxt.setText(R.string.intro5);
                    nextBtn.setText(R.string.start);
                } else {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ActivityIntro.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("first", false);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                    startActivity(intent);
                }
            }
        });
    }
}
