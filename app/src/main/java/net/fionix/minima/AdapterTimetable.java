package net.fionix.minima;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterTimetable extends ArrayAdapter<ClassTimetable> {

    Context mContext;
    private ArrayList<ClassTimetable> dataSet;

    public AdapterTimetable(ArrayList<ClassTimetable> data, Context context) {
        super(context, R.layout.listview_timetable, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.listview_timetable, parent, false);

        ImageView indicator = view.findViewById(R.id.highlight);
        TextView dayTxt = view.findViewById(R.id.textView1);
        TextView courseNameTxt = view.findViewById(R.id.textViewCourseName);
        TextView courseCodeTxt = view.findViewById(R.id.textViewCourseCode);
        TextView groupTxt = view.findViewById(R.id.textViewGroup);
        TextView timeTxt = view.findViewById(R.id.textViewTime);
        TextView locationTxt = view.findViewById(R.id.textViewLocation);
        indicator.setVisibility(View.VISIBLE);

        String courseName = "";
        ArrayList<ClassCourse> arrayListCourseCode = ClassMinima.loadCourse(getContext());
        for (int i = 0; i < arrayListCourseCode.size(); i++) {
            if (arrayListCourseCode.get(i).code.contains(dataSet.get(position).course)) {
                if (arrayListCourseCode.get(i).title.isEmpty())
                    courseName = "You can edit course name at the Course tab";
                else courseName = arrayListCourseCode.get(i).title;
                break;
            }
        }

        courseNameTxt.setText(courseName);
        courseCodeTxt.setText(dataSet.get(position).course);
        groupTxt.setText(dataSet.get(position).group);
        timeTxt.setText(dataSet.get(position).start + " - " + dataSet.get(position).end);

        if (!dataSet.get(position).location.isEmpty())
            locationTxt.setText(dataSet.get(position).location.toUpperCase());
        else locationTxt.setText("-");

        dayTxt.setText(dataSet.get(position).day.substring(0, Math.min(dataSet.get(position).day.toString().length(), 3)).toUpperCase());

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                if (!dataSet.get(position).day.equals("Monday")) {
                    indicator.setVisibility(View.GONE);
                }
                break;

            case Calendar.TUESDAY:
                if (!dataSet.get(position).day.equals("Tuesday")) {
                    indicator.setVisibility(View.GONE);
                }
                break;

            case Calendar.WEDNESDAY:
                if (!dataSet.get(position).day.equals("Wednesday")) {
                    indicator.setVisibility(View.GONE);
                }
                break;

            case Calendar.THURSDAY:
                if (!dataSet.get(position).day.equals("Thursday")) {
                    indicator.setVisibility(View.GONE);
                }
                break;

            case Calendar.FRIDAY:
                if (!dataSet.get(position).day.equals("Friday")) {
                    indicator.setVisibility(View.GONE);
                }
                break;

            case Calendar.SATURDAY:
                if (!dataSet.get(position).day.equals("Saturday")) {
                    indicator.setVisibility(View.GONE);
                }
                break;

            case Calendar.SUNDAY:
                if (!dataSet.get(position).day.equals("Sunday")) {
                    indicator.setVisibility(View.GONE);
                }
                break;
        }

        return view;
    }
}
