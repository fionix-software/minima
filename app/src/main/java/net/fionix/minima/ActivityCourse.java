package net.fionix.minima;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityCourse extends Fragment {

    public static ListView listview;
    public static ProgressBar progressBar;
    public AdapterCourse adapter;
    public SharedPreferences prefs;
    public ArrayList<ClassTimetable> arrayListTimetable;
    public ArrayList<ClassFaculty> arrayListFaculty;
    public ArrayList<ClassCourse> arrayListCourse;
    public Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate view
        View view = inflater.inflate(R.layout.activity_list_course, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Load timetable
        arrayListTimetable = ClassMinima.loadTimetable(getContext());
        arrayListFaculty = ClassMinima.loadFaculty(getContext());
        arrayListCourse = ClassMinima.loadCourse(getContext());

        ClassMinima.JsoupAsyncTaskFetchFaculty jsoupAsyncTaskFetchFaculty = new ClassMinima.JsoupAsyncTaskFetchFaculty();
        jsoupAsyncTaskFetchFaculty.execute();

        // populate data
        adapter = new AdapterCourse(arrayListCourse, getContext());

        // initialize interface components
        button = view.findViewById(R.id.button);
        listview = view.findViewById(R.id.listView);
        listview.setAdapter(adapter);

        // button onClick
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // initialize new dialog box
                Dialog dialogAdd = new Dialog(getContext());
                dialogAdd.setCanceledOnTouchOutside(false);
                dialogAdd.setContentView(R.layout.dialog_add);

                // adding interface component
                final Button buttonAdd = (Button) dialogAdd.findViewById(R.id.button1);
                final EditText editTextCourse = (EditText) dialogAdd.findViewById(R.id.editText1);
                final EditText editTextGroup = (EditText) dialogAdd.findViewById(R.id.editText2);
                progressBar = (ProgressBar) dialogAdd.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);

                // button onClick
                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean exist = false;
                        String group = editTextGroup.getText().toString().trim().toUpperCase();
                        String course = editTextCourse.getText().toString().trim().toUpperCase();

                        // check for the latest data
                        arrayListCourse = ClassMinima.loadCourse(getContext());

                        // check existing course
                        for (int i = 0; i < arrayListCourse.size(); i++) {
                            if (course.equals(arrayListCourse.get(i).code)) {
                                exist = true;
                            }
                        }

                        // check for empty field
                        if (!group.isEmpty() && !course.isEmpty()) {
                            // check for data not existed
                            if (!exist) {
                                // fetching timetable
                                ClassMinima.JsoupAsyncTaskFetchTimetable jsoupAsyncTaskFetchTimetable = new ClassMinima.JsoupAsyncTaskFetchTimetable();
                                jsoupAsyncTaskFetchTimetable.execute(group, course);
                            } else {
                                // notify by toast
                                Toast.makeText(getContext(), "Course already exist in the timetable", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // notify by toast
                            Toast.makeText(getContext(), "Input empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // show dialog box
                dialogAdd.show();
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                // initialize new dialog box
                final Dialog dialogEdit = new Dialog(getContext());
                dialogEdit.setCanceledOnTouchOutside(false);
                dialogEdit.setContentView(R.layout.dialog_edit);

                // check latest data
                arrayListCourse = ClassMinima.loadCourse(getContext());

                // adding interface component
                final Button buttonSave = (Button) dialogEdit.findViewById(R.id.button1);
                final Button buttonDelete = (Button) dialogEdit.findViewById(R.id.button2);
                final EditText editTextCourse = (EditText) dialogEdit.findViewById(R.id.editText1);

                // put the current course name
                editTextCourse.setText(arrayListCourse.get(position).title);

                // button delete onClick
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // delete timetable
                        for (int i = 0; i < arrayListTimetable.size(); i++) {
                            if (arrayListTimetable.get(i).course.equals(arrayListCourse.get(position).code)) {
                                arrayListTimetable.remove(i);
                            }
                        }

                        // delete course and save changes
                        arrayListCourse.remove(position);
                        ClassMinima.saveCourse(arrayListCourse, getContext());
                        ClassMinima.saveTimetable(arrayListTimetable, getContext());

                        // refresh listview
                        adapter = new AdapterCourse(arrayListCourse, getContext());
                        listview.setAdapter(adapter);

                        Toast.makeText(getContext(), "Course deleted", Toast.LENGTH_SHORT).show();

                        dialogEdit.dismiss();
                    }
                });

                // button add onClick
                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editTextCourse.getText().toString().isEmpty()) {

                            // edit course name and save changes
                            arrayListCourse.add(new ClassCourse(arrayListCourse.get(position).faculty, arrayListCourse.get(position).code, arrayListCourse.get(position).group, editTextCourse.getText().toString()));
                            arrayListCourse.remove(position);
                            ClassMinima.saveCourse(arrayListCourse, getContext());

                            // refresh listview
                            adapter = new AdapterCourse(arrayListCourse, getContext());
                            listview.setAdapter(adapter);

                            Toast.makeText(getContext(), "Save successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Field empty", Toast.LENGTH_SHORT).show();
                        }
                        dialogEdit.dismiss();
                    }
                });

                dialogEdit.show();

                return true;
            }
        });

        return view;
    }
}
