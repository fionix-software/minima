package net.fionix.minima;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class ActivityTimetable extends Fragment {

    public static ListView listview;
    public static ProgressBar progressBar;
    public AdapterTimetable adapter;
    public ArrayList<ClassTimetable> arrayListTimetable;
    public Button button;
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate view
        View view = inflater.inflate(R.layout.activity_list_timetable, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        // populate Data
        arrayListTimetable = ClassMinima.loadTimetable(getContext());
        adapter = new AdapterTimetable(arrayListTimetable, getContext());

        // initialize interface component
        listview = view.findViewById(R.id.listView);
        listview.setAdapter(adapter);
        button = view.findViewById(R.id.button);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // button onClick
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // recheck timetable
                ClassMinima.JsoupAsyncTaskRefetchTimetable jsoupAsyncTaskRefetchTimetable = new ClassMinima.JsoupAsyncTaskRefetchTimetable();
                jsoupAsyncTaskRefetchTimetable.execute();
            }
        });

        return view;
    }
}
