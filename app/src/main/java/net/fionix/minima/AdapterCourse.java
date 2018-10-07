package net.fionix.minima;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nazebzurati on 16/09/2017.
 */

public class AdapterCourse extends ArrayAdapter<ClassCourse> {

    private final ArrayList<ClassCourse> items;

    public AdapterCourse(ArrayList<ClassCourse> data, Context context) {
        super(context, R.layout.listview_timetable, data);
        this.items = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.listview_course, parent, false);

        TextView facTxt = view.findViewById(R.id.textView2);
        TextView courseNameTxt = view.findViewById(R.id.textView3);
        TextView courseTxt = view.findViewById(R.id.textView4);
        TextView groupTxt = view.findViewById(R.id.textView5);

        facTxt.setText(items.get(position).faculty);
        courseTxt.setText(items.get(position).code);
        groupTxt.setText(items.get(position).group);

        if (items.get(position).title.isEmpty())
            courseNameTxt.setText("Long press here to change course name.");
        else courseNameTxt.setText(items.get(position).title);

        return view;
    }
}
